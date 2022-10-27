/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;
import Entidades.Persona;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controller.CitaVerController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.Response;
import okhttp3.internal.http2.Settings;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author alexis
 */
public class HttpMethods {

    Gson json = new Gson();
    HttpClient httpclient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    String url = "http://localhost:8000/";//;
    final String DATA = "data";
    final String ADDRESS = "address";
    final String NOMBREDISPOSITIVO = "nombreDispositivo";
    UtilClass oUtilClass = new UtilClass();
    String X_CSRFToken = "";
    String Cookie = "";

    public HttpMethods() {
        X_CSRFToken = getCSRFToken();
        Cookie = getCokkie();
        url = oUtilClass.leerTXT("server.txt");
    }

    public String setCSRFToken(String CSRFtokenNew) {
        return oUtilClass.updateArchivo("csfr", CSRFtokenNew);
    }

    public String getCSRFToken() {
        return oUtilClass.leerTXT("csfr");
    }

    public String setCokkie(String CokkieNew) {
        return oUtilClass.updateArchivo("cokkie", CokkieNew);
    }

    public String getCokkie() {
        return oUtilClass.leerTXT("cokkie");

    }

    HttpResponse<String> procesoHttpPOST(String metodo, String obj) {
        try {
            HttpRequest requestPosts = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(obj))
                    .uri(URI.create(url + metodo))
                    .setHeader("Content-type", "application/json")
                    .setHeader("X-CSRFToken", X_CSRFToken)
                    .setHeader("Cookie", Cookie).build();
            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            return response;

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(HttpMethods.class.getName()).log(Level.SEVERE, ex.getMessage(), ex.getMessage());
        }
        return null;
    }

    HttpResponse<String> procesoHttpGET(String metodo) {
        try {
            HttpRequest requestPosts = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + metodo))
                    .setHeader("Content-type", "application/json")
                    .setHeader("X-CSRFToken", X_CSRFToken)
                    .setHeader("Cookie", Cookie).build();
            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            return response;

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(HttpMethods.class.getName()).log(Level.SEVERE, ex.getMessage(), ex.getMessage());
        }
        return null;
    }

    //el tipo object en este método recibe String y Persona
    
    final String citaUrl="cita";
    final String sessionUrl="session";
    public HttpResponse<String> loguear(String nickname, String contrasenia) {
        JsonObject Objson = new JsonObject();
        Objson.addProperty("username", nickname);
        Objson.addProperty("password", contrasenia);
        return procesoHttpPOST(sessionUrl+"/loguear", Objson.toString());
    }
    
    public HttpResponse<String> CerrarSesion(){
        return procesoHttpGET(sessionUrl+"/CerrarSesion");
    }

    public <T> List<T> getList(Class<T> generico, String metodo) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();

        HttpResponse<String> response = procesoHttpGET(citaUrl+metodo);
        listGenericos = json.fromJson(response.body(), type);

        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }
        return listGenericos2;
    }

    public <T> List<T> getCitaFilter(Class<T> generico, String metodo, JsonObject citaAtributesJson) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();

        HttpResponse<String> response = procesoHttpPOST(citaUrl+metodo, citaAtributesJson.toString());
        listGenericos = json.fromJson(response.body(), type);

        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }

        return listGenericos2;
    }

    public <T> HttpResponse<String> AddObject(Class<T> generico, Object objeto, String metodo) {
        T obj = (T) objeto;
        String jsonResponse = json.toJson(obj);
        return procesoHttpPOST(citaUrl+metodo, jsonResponse.toString());
    }

    public <T> String UpdateObject(Class<T> generico, Object objeto, String metodo) {
        T obj = (T) objeto;
        String jsonResponse = json.toJson(obj);
        String responseRPTA = "fail";
        HttpResponse<String> response = procesoHttpPOST(citaUrl+metodo, jsonResponse.toString());
        responseRPTA = response.body();
        return responseRPTA;
    }

    public <T> HttpResponse<String> DeleteObject(Class<T> generico, String metodo, String var) {
        JsonObject Objson = new JsonObject();
        Objson.addProperty("id", var);
        return procesoHttpPOST(citaUrl+metodo, Objson.toString());
    }

    public void getAddress() {
        JsonObject Objson = new JsonObject();
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(Objson.toString()))
                .uri(URI.create(url + "GetAddress")).build();
        //Address oAddress = null;
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
        //    oAddress = json.fromJson(response.body(), Address.class);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //return oAddress;
    }

    public String getMACAddress() {

        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            NetworkInterface networkInterface;
            networkInterface = NetworkInterface
                    .getByInetAddress(ipAddress);
            byte[] macAddressBytes = networkInterface.getHardwareAddress();
            StringBuilder macAddressBuilder = new StringBuilder();
            for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++) {
                String macAddressHexByte = String.format("%02X",
                        macAddressBytes[macAddressByteIndex]);
                macAddressBuilder.append(macAddressHexByte);

                if (macAddressByteIndex != macAddressBytes.length - 1) {
                    macAddressBuilder.append(":");
                }

            }
            return macAddressBuilder.toString();

        } catch (SocketException ex) {
            Logger.getLogger(HttpMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
            return "ERROR ADDRESS";

        } catch (UnknownHostException ex) {
            Logger.getLogger(HttpMethods.class
                    .getName()).log(Level.SEVERE, null, ex);
            return "ERROR ADDRESS";
        }

    }

    String getNombrePc() {
        String nombrepc = "sin nombre";
        try {
            nombrepc = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.out.println(e.toString());

        }
        return nombrepc;
    }

}
