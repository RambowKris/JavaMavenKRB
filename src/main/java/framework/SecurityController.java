/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import com.google.common.io.ByteStreams;
import framework.compute.ComputeManager;
import framework.confidentiality.AuthenticationManager;
import framework.confidentiality.AuthorizationManager;
import framework.confidentiality.TenantConfiguration;
import framework.integrity.IntegrityManager;
import framework.network.ClientRequest;
import framework.storage.StorageManager;
import framework.support.DataConverter;
import framework.support.ExecutionManager;
import framework.support.YamlConfiguration;
import java.io.PrintWriter;
import java.util.UUID;
import org.glassfish.samples.RequestLogEJB;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.samples.CloudPermissionEJB;
import org.glassfish.samples.CloudUserEJB;
import org.glassfish.samples.IntegrityDataEJB;
import org.glassfish.samples.TenantEJB;

/**
 *
 * @author krb
 */
//@ManagedBean
public class SecurityController {

//    @EJB
    private RequestLogEJB requestLogBean;
    private IntegrityDataEJB integrityDataBean;
    private CloudUserEJB cloudUserBean;
    private TenantEJB tenantBean;
    private CloudPermissionEJB cloudPermissionBean;

    private AuthenticationManager authenticationManager;

    private TenantConfiguration tenantConfig;

    private HttpServletResponse httpServletResponse;
    private ClientRequest request;

    private UUID requestId;

    public SecurityController(
            ClientRequest request,
            HttpServletResponse httpServletResponse,
            RequestLogEJB requestLogBean,
            IntegrityDataEJB integrityDataBean,
            CloudUserEJB cloudUserBean,
            TenantEJB tenantBean,
            CloudPermissionEJB cloudPermissionBean
    ) {
        this.request = request;
        this.httpServletResponse = httpServletResponse;
        this.requestLogBean = requestLogBean;
        this.integrityDataBean = integrityDataBean;
        this.cloudUserBean = cloudUserBean;
        this.tenantBean = tenantBean;
        this.cloudPermissionBean = cloudPermissionBean;
    }

    public void runFramework() {
        this.requestId = UUID.randomUUID();
        int incomingTime = (int) System.currentTimeMillis();

        this.authenticationManager = new AuthenticationManager(this.cloudUserBean);
        if (!authenticationManager.authenticate(this.request.getAuthentication())) {
            this.triggerError("The request could not be authenticated", 401, incomingTime);
        }
        System.out.println("Authentication Succeeded");

        AuthorizationManager authorizationManager = new AuthorizationManager(this.tenantBean, this.cloudPermissionBean);
        if (!authorizationManager.authorize(this.authenticationManager.getTenantName(), this.request.getService())) {
            this.triggerError("The requested service is not allowed", 403, incomingTime);
        }
        System.out.println("Authorization Succeeded");

        String cloudConfig = authorizationManager.getTenant().getCloudConfig();
        this.tenantConfig = (TenantConfiguration) DataConverter.getObjectFromJson(cloudConfig, TenantConfiguration.class);

        byte[] result = null;
        if (this.request.getService().equalsIgnoreCase("storage")) {
            result = this.storageFlow(incomingTime);
        } else {
            result = this.computationFlow(incomingTime);
        }

        if (result != null) {
            try {
                this.httpServletResponse.getOutputStream().write(result);

//                PrintWriter pw = this.httpServletResponse.getWriter();
//                pw.write(result);
//                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }

        }

        int outgoingTime = (int) System.currentTimeMillis();
        this.requestLogBean.create(incomingTime, this.requestId.toString(), outgoingTime);
    }

    private byte[] storageFlow(int incomingTime) {
        StorageManager storageManager = new StorageManager();

        String[] storageClouds = this.tenantConfig.getStorageClouds();
        for (int c = 0; c < storageClouds.length; c++) {
            storageClouds[c] += "-" + this.authenticationManager.getTenantName();
        }

        storageManager.setParties(storageClouds);
        storageManager.setSplitFunction(this.tenantConfig.getSplitFunction());

        byte[] result = null;
        if (this.request.getCommand().equalsIgnoreCase("store")) {
            ExecutionManager.executeMethodOnObject(storageManager, this.request.getCommand(), new Object[]{this.request.getKeyName(), this.request.getData()});

            if (storageManager.hasSuccess()) {
                result = "Success".getBytes();
            }
        } else {
            result = (byte[]) ExecutionManager.executeMethodOnObject(storageManager, this.request.getCommand(), new Object[]{this.request.getKeyName()});
        }

        if (!storageManager.hasSuccess()) {
            this.triggerError("The command could not be executed", 400, incomingTime);
        }
        System.out.println("Command Executed");

        IntegrityManager integrityManager = new IntegrityManager(this.integrityDataBean);
        if (!integrityManager.checkDataIntegrity(this.request.getCommand(), this.request.getKeyName(), result == null ? this.request.getData() : result)) {
            this.triggerError("The integrity check failed", 409, incomingTime);
        }
        System.out.println("Integrity Succeeded");

        return result;
    }

    private byte[] computationFlow(int incomingTime) {
        ComputeManager computeManager = new ComputeManager();

        IntegrityManager integrityManager = new IntegrityManager(this.integrityDataBean);
        if (!integrityManager.checkServiceIntegrity(this.request.getService() + "Manager", this.request.getCommand())) {
            this.triggerError("The integrity check failed", 409, incomingTime);
        }
        System.out.println("Integrity Succeeded");

        String result = null;
        String keyName = this.request.getKeyName() == null ? "" : this.request.getKeyName();
        result = (String) ExecutionManager.executeMethodOnObject(computeManager, this.request.getCommand(), new Object[]{keyName, this.request.getData()});
        if (!computeManager.hasSuccess()) {
            this.triggerError("The command could not be executed", 400, incomingTime);
        }
        System.out.println("Command Executed");

        return result.getBytes();
    }

    private void triggerError(String message, int errorCode, int incomingTime) {
        int outgoingTime = (int) System.currentTimeMillis();

        this.requestLogBean.create(incomingTime, this.requestId.toString(), outgoingTime);

        Response response = Response
                .status(errorCode)
                //                    .status(Response.Status.BAD_REQUEST)
                .entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build();

        throw new WebApplicationException(message, response);

    }

}
