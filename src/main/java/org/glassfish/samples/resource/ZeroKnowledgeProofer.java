/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.resource;

import framework.support.Crypto;
import framework.support.DataConverter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.samples.CloudUserEJB;
import org.glassfish.samples.ZeroKnowledgeSessionEJB;
import org.glassfish.samples.ZeroKnowledgeUserEJB;
import org.glassfish.samples.model.ZeroKnowledgeSession;
import org.glassfish.samples.model.ZeroKnowledgeUser;

/**
 * REST Web Service
 *
 * @author krb
 */
@ManagedBean
@Path("zkp")
public class ZeroKnowledgeProofer {

    @Context
    private HttpServletRequest httpServletRequest;

    @Context
    private HttpServletResponse httpServletResponse;

    @EJB
    ZeroKnowledgeUserEJB zeroKnowledgeUserBean;

    @EJB
    ZeroKnowledgeSessionEJB zeroKnowledgeSessionBean;

    @EJB
    CloudUserEJB cloudUserBean;

    private ZeroKnowledgeUser zku = null;
    private ZeroKnowledgeSession zks = null;

    /**
     * Creates a new instance of ZeroKnowledgeProofer
     */
    public ZeroKnowledgeProofer() {
    }

    /**
     * Retrieves representation of an instance of
     * org.glassfish.samples.resource.ZeroKnowledgeProofer
     *
     * @return an instance of HttpServletResponse
     */
    @GET
    @Path("{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpServletResponse getJson(
            @PathParam("userName") String userName
    ) {
        this.checkZeroKnowledgeUser(userName);

        BigInteger a = new BigInteger(12, new SecureRandom());
        UUID id = UUID.randomUUID();
        int time = (int) System.currentTimeMillis();

        this.zeroKnowledgeSessionBean.create(id.toString(), userName, a.toString(), "" + time);

        System.out.println("Session ID: " + id);

        try {
            this.httpServletResponse.setHeader("Set-Cookie", "zkp=" + id);
            PrintWriter pw = this.httpServletResponse.getWriter();
            pw.write(a.toString());
            pw.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return this.httpServletResponse;
    }

    /**
     * Retrieves representation of an instance of
     * org.glassfish.samples.resource.ZeroKnowledgeProofer
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "hello world!";
    }

    /**
     * PUT method for updating or creating an instance of ZeroKnowledgeProofer
     *
     */
    @PUT
    @Path("{userName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpServletResponse putJson(@Context HttpServletRequest httpRequest, @Context HttpServletResponse httpResponse, @PathParam("userName") String userName, String content) {
        this.checkZeroKnowledgeUser(userName);

        this.checkCookieForZeroKnowledgeSessionId(userName, this.httpServletRequest.getHeader("cookie"));

        String wx = "";

        String queryString = this.httpServletRequest.getQueryString();
        String[] queries = queryString.split("&");
        for (String query : queries) {
            String[] queryValues = query.split("=");
            if (queryValues[0].equalsIgnoreCase("wx")) {
                wx = queryValues[1];
            }
        }

        String cClient = content;
        BigInteger cBigInteger = new BigInteger(cClient, 16);

        String serverString = this.calculateServerString(cBigInteger, new BigInteger(wx, 16));

        if (!serverString.equalsIgnoreCase(serverString)) {
            String message = "Request not allowed.";

            Response response = Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

            throw new WebApplicationException(message, response);

        }

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[64];
        random.nextBytes(bytes);
        String authenticationToken = DataConverter.byteArray2Hex(bytes);

        this.cloudUserBean.updateUserPassword(userName, authenticationToken);

        System.out.println("Authentication Token: " + authenticationToken);

        try {
            this.httpServletResponse.setHeader("Set-Cookie", "zkp-token=" + authenticationToken);
            PrintWriter pw = this.httpServletResponse.getWriter();
            pw.write("Token: " + authenticationToken + "\n");
            pw.write("Token length: " + authenticationToken.length() + "\n");
            pw.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return this.httpServletResponse;
    }

    private void checkCookieForZeroKnowledgeSessionId(String userName, String cookie) {
        String id = null;

        String[] cookieStrings = cookie.split("; ");
        for (String cookieString : cookieStrings) {
            String[] cookieValues = cookieString.split("=");
            if (cookieValues[0].equalsIgnoreCase("zkp")) {
                id = cookieValues[1];
            }
        }

        if (id == null) {
            String message = "Request not allowed.";

            Response response = Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

            throw new WebApplicationException(message, response);
        }

        this.zks = this.zeroKnowledgeSessionBean.getZeroKnowledgeSessionByUserNameAndId(userName, id);

        if (this.zks == null) {
            String message = "Request not allowed.";

            Response response = Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

            throw new WebApplicationException(message, response);
        }

        this.zeroKnowledgeSessionBean.deleteZeroKnowledgeSessionById(id);

        this.zku = this.zeroKnowledgeUserBean.getZeroKnowledgeUserByUserName(zks.getUserName());

        if (this.zku == null) {
            String message = "Request not allowed.";

            Response response = Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

            throw new WebApplicationException(message, response);
        }

    }

    private void checkZeroKnowledgeUser(String userName) {
        ZeroKnowledgeUser zku = this.zeroKnowledgeUserBean.getZeroKnowledgeUserByUserName(userName);

        if (zku == null) {
            String message = "Request not allowed.";

            Response response = Response
                    .status(Response.Status.FORBIDDEN)
                    .entity(message)
                    .type(MediaType.TEXT_PLAIN)
                    .build();

            throw new WebApplicationException(message, response);

        }
    }

    private String calculateServerString(BigInteger cBigInteger, BigInteger wxBigInteger) {
        BigInteger g0BigInteger = new BigInteger(this.zku.getG0(), 16);
        BigInteger r2BigInteger = new BigInteger(this.zku.getR2(), 16);
        BigInteger hashBigInteger = new BigInteger(this.zku.getHashValue(), 16);

        BigInteger ta = hashBigInteger.modPow(cBigInteger, r2BigInteger);
        BigInteger tb = g0BigInteger.modPow(wxBigInteger, r2BigInteger);
        BigInteger tc = ta.multiply(tb);
        BigInteger ts = tc.mod(r2BigInteger);

        String newStringServer = this.zku.getHashValue() + ts.toString() + this.zks.getA(); // string of y concat ts concat a
        return Crypto.calculateHashToHexString("SHA1", newStringServer);
    }
}
