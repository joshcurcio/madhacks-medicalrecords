package com.badass.josh.medicalrecords;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.client.methods.HttpPut;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;

import java.net.URI;
import java.util.stream.*;

import static java.lang.System.in;

/**
 * Created by Josh on 10/7/2017.
 */

public class FaceAPI {
    private static final String SUBSCRIPTION_KEY = "13hc77781f7e4b19b5fcdd72a8df7156";
    private static final String DETECT_BASE = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
    private static final String PERSON_GROUP_BASE = "https://eastus.api.cognitive.microsoft.com/face/v1.0/personGroups/";

    private static String TAG = "FACE API: ";


    private FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(SUBSCRIPTION_KEY);


    public void createPersonGroup(String personGroup){
        HttpClient httpClient = new DefaultHttpClient();

        try
        {

            URIBuilder builder = new URIBuilder(PERSON_GROUP_BASE +
                    personGroup);

            URI uri = builder.build();
            HttpPut request = new HttpPut(uri);

            // Request headers. Replace the example key with your valid subscription key.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);

            // Request body. The name field is the display name you want for the group (must be under 128 characters).
            // The size limit for what you want to include in the userData field is 16KB.
            String body = "{ \"name\":\"My Group\",\"userData\":\"User-provided data attached to the person group.\" }";

            StringEntity reqEntity = new StringEntity(body);
            request.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void indentifyPatient(String picLocation){
        /*try
        {
            InputStream fis = new InputStream(picLocation);
            faceServiceClient.identity(fis);
            var faces = await faceServiceClient.DetectAsync(s);
            var faceIds = faces.Select(face => face.FaceId).ToArray();

            var results = await faceServiceClient.IdentifyAsync(personGroupId, faceIds);
            foreach (var identifyResult in results)
            {
                Console.WriteLine("Result of face: {0}", identifyResult.FaceId);
                if (identifyResult.Candidates.Length == 0)
                {
                    Console.WriteLine("No one identified");
                }
                else
                {
                    // Get top 1 among all candidates returned
                    var candidateId = identifyResult.Candidates[0].PersonId;
                    var person = await faceServiceClient.GetPersonAsync(personGroupId, candidateId);
                    Console.WriteLine("Identified as {0}", person.Name);
                }
            }
        } catch (Exception ex) {

        }*/
    }


    public void retrieveUserIdFromPicture(String picLocation){
        HttpClient httpclient = new DefaultHttpClient();

        try
        {
            URIBuilder builder = new URIBuilder(DETECT_BASE);

            // Request parameters. All of them are optional.
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", SUBSCRIPTION_KEY);

            // Request body.
            StringEntity reqEntity = new StringEntity("{\"url\": \"picLocation\"}");
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                // Format and display the JSON response.
                System.out.println("REST Response:\n");

                String jsonString = EntityUtils.toString(entity).trim();
                if (jsonString.charAt(0) == '[') {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    System.out.println(jsonArray.toString(2));
                }
                else if (jsonString.charAt(0) == '{') {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    System.out.println(jsonObject.toString(2));
                } else {
                    System.out.println(jsonString);
                }
            }
        }
        catch (Exception e)
        {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
}
