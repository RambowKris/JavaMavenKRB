/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.samples.resource;

import com.google.common.io.ByteStreams;
import framework.SecurityController;
import framework.deployment.GcInstance;
import framework.network.ClientRequest;
import framework.storage.FileStorage;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import framework.storage.StorageManager;
import framework.support.Crypto;
import framework.support.DataConverter;
import framework.support.YamlConfiguration;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.io.IOUtils;
import org.glassfish.samples.CloudPermissionEJB;
import org.glassfish.samples.CloudUserEJB;
//import org.glassfish.samples.DatabaseMetaDataEJB;
import org.glassfish.samples.DbStorageEJB;
import org.glassfish.samples.IntegrityDataEJB;
import org.glassfish.samples.RequestLogEJB;
import org.glassfish.samples.TenantEJB;
//import org.glassfish.samples.model.ExtractDatabaseMetaData;

/**
 * REST Web Service
 *
 * @author krb
 */
@ManagedBean
@Path("storage")
public class ApiStorage {

    @Context
    UriInfo context;

    @Context
    HttpServletResponse httpServletResponse;

    @Context
    HttpServletRequest httpServletRequest;

    @EJB
    RequestLogEJB requestLogBean;

    @EJB
    IntegrityDataEJB integrityDataBean;

    @EJB
    DbStorageEJB dbStorageBean;

    @EJB
    CloudUserEJB cloudUserBean;

    @EJB
    CloudPermissionEJB cloudPermissionBean;

    @EJB
    TenantEJB tenantBean;

//    @EJB
//    DatabaseMetaDataEJB databaseMetaDataBean;
    /**
     * Creates a new instance of SimpleResource
     */
    public ApiStorage() {
    }

    /**
     * Retrieves representation of an instance of
     * org.glassfish.samples.resource.ApiStorage
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{keyName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    @Produces(MediaType.APPLICATION_JSON)
    public HttpServletResponse getJson(
            @PathParam("keyName") String keyName,
            Request request
    ) {
        ClientRequest clientRequest = new ClientRequest(this.httpServletRequest, "find", null);
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

    /**
     * Retrieves representation of an instance of
     * org.glassfish.samples.resource.ApiStorage
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HttpServletResponse getJson() {
//    public String getJson(@HeaderParam("cookie") String cookie, @HeaderParam("user-agent") String userAgent) {
        //TODO return proper representation object
//        throw new UnsupportedOperationException();
//        return "{\"firstName\":\"Anders\",\"lastName\":\"Johnson\",\"age\":40}";

        String render = "test\n";

//        FileStorage fs = new FileStorage("testFile.txt");
//        fs.createFile();
//        render += "File Path: " + fs.getAbsoluteFilePath() + "\n";
//        render += this.databaseMetaDataBean.getMetaData() + "\n";
//        try {
//            Connection con = ExtractDatabaseMetaData.getOracleConnection();
//            DatabaseMetaData meta = con.getMetaData();
//            ResultSet schemas = meta.getSchemas();
//            while (schemas.next()) {
//                String tableSchema = schemas.getString(1);    // "TABLE_SCHEM"
//                String tableCatalog = schemas.getString(2); //"TABLE_CATALOG"
//                render += "tableSchema" + tableSchema;
////                System.out.println("tableSchema" + tableSchema);
//                render += "tableCatalog" + tableCatalog;
////                System.out.println("tableCatalog" + tableCatalog);
//            }
//
//            con.close();
//        } catch (Exception e) {
//            System.err.println("Exception: " + e.getMessage());
//        }
//        InputStream output = null;
//        try {
//            output = new ByteArrayInputStream(render.getBytes(StandardCharsets.UTF_8.name()));
//        } catch (Exception e) {
//        }
//        return output;
        try {
            PrintWriter pw = this.httpServletResponse.getWriter();
            pw.write(DataConverter.encodeToJson("Success 12"));
            pw.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return this.httpServletResponse;
//        return integrityDataBean.getList().toString();
    }

    /**
     * PUT method for updating or creating an instance of ApiStorage
     *
     * @param content representation for the resource
     */
    @PUT
    @Path("{keyName}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
//    @Consumes(MediaType.APPLICATION_JSON)
    public HttpServletResponse putJson(
            @Context HttpServletRequest httpRequest,
            @Context HttpServletResponse httpResponse,
            @PathParam("keyName") String keyName,
            byte[] content
    ) {
        
        ClientRequest clientRequest = new ClientRequest(this.httpServletRequest, "store", content);
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

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpServletResponse deleteJson() {
        return this.httpServletResponse;
    }

}
