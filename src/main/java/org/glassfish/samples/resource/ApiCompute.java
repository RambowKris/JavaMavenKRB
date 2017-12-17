/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.resource;

import framework.SecurityController;
import framework.network.ClientRequest;
import framework.support.DataConverter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.glassfish.samples.CloudPermissionEJB;
import org.glassfish.samples.CloudUserEJB;
import org.glassfish.samples.IntegrityDataEJB;
import org.glassfish.samples.RequestLogEJB;
import org.glassfish.samples.TenantEJB;

/**
 * REST Web Service
 *
 * @author krb
 */
@ManagedBean
@Path("compute")
public class ApiCompute {

    @Context
    private UriInfo context;

    @Context
    HttpServletResponse httpServletResponse;

    @Context
    HttpServletRequest httpServletRequest;

    @EJB
    RequestLogEJB requestLogBean;

    @EJB
    IntegrityDataEJB integrityDataBean;

    @EJB
    CloudUserEJB cloudUserBean;

    @EJB
    CloudPermissionEJB cloudPermissionBean;

    @EJB
    TenantEJB tenantBean;

    /**
     * Creates a new instance of ApiCompute
     */
    public ApiCompute() {
    }

    /**
     * Retrieves representation of an instance of
     * org.glassfish.samples.resource.ApiCompute
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{keyName}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpServletResponse getJson(
            @Context HttpServletRequest httpRequest,
            @Context HttpServletResponse httpResponse,
            @PathParam("keyName") String keyName
    ) {
        ClientRequest clientRequest = new ClientRequest(this.httpServletRequest, "getExecute", null);
//        ClientRequest clientRequest = new ClientRequest(this.httpServletRequest, "execute", content);
    
        SecurityController sc = new SecurityController(
                clientRequest,
                this.httpServletResponse,
                this.requestLogBean,
                this.integrityDataBean,
                this.cloudUserBean,
                this.tenantBean,
                this.cloudPermissionBean
        );
        sc.runFramework();

//        try {
//            PrintWriter pw = this.httpServletResponse.getWriter();
//            pw.write(DataConverter.encodeToJson("Success compute get with key name"));
//            pw.close();
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
        
        return this.httpServletResponse;
    }

    /**
     * Retrieves representation of an instance of
     * org.glassfish.samples.resource.ApiCompute
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HttpServletResponse getJson() {
        try {
            PrintWriter pw = this.httpServletResponse.getWriter();
            pw.write(DataConverter.encodeToJson("Success compute get"));
            pw.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        return this.httpServletResponse;
    }

    /**
     * PUT method for updating or creating an instance of ApiCompute
     *
     * @param content representation for the resource
     */
    @PUT
    @Path("{keyName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpServletResponse putJson(
            @PathParam("keyName") String keyName,
            byte[] content
    ) {
        ClientRequest clientRequest = new ClientRequest(this.httpServletRequest, "putHaveFun", content);
//        ClientRequest clientRequest = new ClientRequest(this.httpServletRequest, "execute", content);
        SecurityController sc = new SecurityController(
                clientRequest,
                this.httpServletResponse,
                this.requestLogBean,
                this.integrityDataBean,
                this.cloudUserBean,
                this.tenantBean,
                this.cloudPermissionBean
        );
        sc.runFramework();
               
        return this.httpServletResponse;
    }
}
