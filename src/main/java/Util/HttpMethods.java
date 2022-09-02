/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import EntidadesSettings.SettingsDoctor;
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
    String url = "http://localhost:5000/";//;
    String address = "NADA";
    String nombreDispositivo = "NADA";
    final String DATA = "data";
    final String ADDRESS = "address";
    final String NOMBREDISPOSITIVO = "nombreDispositivo";
    UtilClass oUtilClass=new UtilClass();

    public HttpMethods() {
        nombreDispositivo = getNombrePc();
        address = getMACAddress();
        url=oUtilClass.leerTXT("server.txt");
    }

    public <T> List<T> getList(Class<T> generico, String metodo) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();
        JsonObject Objson = new JsonObject();
        Objson.addProperty(ADDRESS, address);
        Objson.addProperty(NOMBREDISPOSITIVO, nombreDispositivo);
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(Objson.toString()))
                .uri(URI.create(url + metodo)).build();
        try {
            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());

            listGenericos = json.fromJson(response.body(), type);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }
        return listGenericos2;
    }

    public <T> List<T> getCitaByFecha(Class<T> generico, String metodo, String fecha) {
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();

        JsonObject Objson = new JsonObject();
        Objson.addProperty(ADDRESS, address);
        Objson.addProperty(NOMBREDISPOSITIVO, nombreDispositivo);
        Objson.addProperty("fecha", fecha);
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(Objson.toString()))
                .uri(URI.create(url + metodo)).build();
        List<SettingsDoctor> LIST;
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            listGenericos = json.fromJson(response.body(), type);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }

        return listGenericos2;
    }

    public <T> List<T> getCitaFilter(Class<T> generico, String metodo, JsonObject citaAtributesJson) {
        citaAtributesJson.addProperty(ADDRESS, address);
        citaAtributesJson.addProperty(NOMBREDISPOSITIVO, nombreDispositivo);
        Type type = new TypeToken<List<T>>() {
        }.getType();
        List<T> listGenericos = new ArrayList<T>();
        List<T> listGenericos2 = new ArrayList<T>();
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(citaAtributesJson.toString()))
                .uri(URI.create(url + metodo)).build();
        List<SettingsDoctor> LIST;
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            listGenericos = json.fromJson(response.body(), type);
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (T listGenerico : listGenericos) {
            listGenericos2.add(json.fromJson(json.toJson(listGenerico), generico));
        }

        return listGenericos2;
    }

    public <T> String AddObject(Class<T> generico, Object objeto, String metodo) {
        T obj = (T) objeto;
        String jsonResponse = json.toJson(obj);
        JsonObject Objson = new JsonObject();
        Objson.addProperty(ADDRESS, address + "");
        Objson.addProperty(NOMBREDISPOSITIVO, nombreDispositivo);
        Objson.addProperty(DATA, jsonResponse);
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(Objson.toString()))
                .uri(URI.create(url + metodo)).build();
        String responseRPTA = "fail";
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            responseRPTA = response.body();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseRPTA;

    }

    public <T> String UpdateObject(Class<T> generico, Object objeto, String metodo) {
        T obj = (T) objeto;
        String jsonResponse = json.toJson(obj);
        JsonObject Objson = new JsonObject();
        Objson.addProperty(ADDRESS, address);
        Objson.addProperty(NOMBREDISPOSITIVO, nombreDispositivo);
        Objson.addProperty(DATA, jsonResponse);
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(Objson.toString()))
                .uri(URI.create(url + metodo)).build();
        String responseRPTA = "fail";
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());
            responseRPTA = response.body();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return responseRPTA;
    }

    public <T> String DeleteObject(Class<T> generico, String metodo, String var) {
        JsonObject Objson = new JsonObject();
        Objson.addProperty(ADDRESS, address);
        Objson.addProperty(NOMBREDISPOSITIVO, nombreDispositivo);
        Objson.addProperty("id", var);
        HttpRequest requestPosts = HttpRequest.newBuilder().header("Content-type", "application/json").POST(HttpRequest.BodyPublishers.ofString(Objson.toString()))
                .uri(URI.create(url + metodo)).build();
        try {

            HttpResponse<String> response = httpclient.send(requestPosts, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(CitaVerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "ok";
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
            Logger.getLogger(HttpMethods.class.getName()).log(Level.SEVERE, null, ex);
            return "ERROR ADDRESS";
        } catch (UnknownHostException ex) {
            Logger.getLogger(HttpMethods.class.getName()).log(Level.SEVERE, null, ex);
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
