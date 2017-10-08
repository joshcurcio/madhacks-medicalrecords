package com.badass.josh.medicalrecords;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceClient.FaceAttributeType;
import com.microsoft.projectoxford.face.FaceServiceClient.FindSimilarMatchMode;
import com.microsoft.projectoxford.face.common.RequestMethod;
import com.microsoft.projectoxford.face.contract.AddPersistedFaceResult;
import com.microsoft.projectoxford.face.contract.CreatePersonResult;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceList;
import com.microsoft.projectoxford.face.contract.FaceListMetadata;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.GroupResult;
import com.microsoft.projectoxford.face.contract.IdentifyResult;
import com.microsoft.projectoxford.face.contract.Person;
import com.microsoft.projectoxford.face.contract.PersonFace;
import com.microsoft.projectoxford.face.contract.PersonGroup;
import com.microsoft.projectoxford.face.contract.SimilarFace;
import com.microsoft.projectoxford.face.contract.SimilarPersistedFace;
import com.microsoft.projectoxford.face.contract.TrainingStatus;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.rest.ClientException;
import com.microsoft.projectoxford.face.rest.WebServiceRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FacesServiceImpl {
    private final WebServiceRequest mRestCall;
    private Gson mGson;
    private static final String DEFAULT_API_ROOT = "https://eastus.api.cognitive.microsoft.com/face/v1.0";
    private final String mServiceHost;
    private static final String DETECT_QUERY = "detect";
    private static final String VERIFY_QUERY = "verify";
    private static final String TRAIN_QUERY = "train";
    private static final String TRAINING_QUERY = "training";
    private static final String IDENTIFY_QUERY = "identify";
    private static final String PERSON_GROUPS_QUERY = "persongroups";
    private static final String PERSONS_QUERY = "persons";
    private static final String FACE_LISTS_QUERY = "facelists";
    private static final String PERSISTED_FACES_QUERY = "persistedfaces";
    private static final String GROUP_QUERY = "group";
    private static final String FIND_SIMILARS_QUERY = "findsimilars";
    private static final String STREAM_DATA = "application/octet-stream";
    private static final String DATA = "data";

    public FacesServiceImpl(String subscriptionKey) {
        this("https://eastus.api.cognitive.microsoft.com/face/v1.0", subscriptionKey);
    }

    public FacesServiceImpl(String serviceHost, String subscriptionKey) {
        this.mGson = (new GsonBuilder()).setDateFormat("M/d/yyyy h:m:s a").create();
        this.mServiceHost = serviceHost.replaceAll("/$", "");
        this.mRestCall = new WebServiceRequest(subscriptionKey);
    }

    public Face[] detect(String url, boolean returnFaceId, boolean returnFaceLandmarks, FaceAttributeType[] returnFaceAttributes) throws ClientException, IOException {
        HashMap params = new HashMap();
        params.put("returnFaceId", Boolean.valueOf(returnFaceId));
        params.put("returnFaceLandmarks", Boolean.valueOf(returnFaceLandmarks));
        if(returnFaceAttributes != null && returnFaceAttributes.length > 0) {
            StringBuilder path = new StringBuilder();
            boolean uri = true;
            FaceAttributeType[] json = returnFaceAttributes;
            int listType = returnFaceAttributes.length;

            for(int faces = 0; faces < listType; ++faces) {
                FaceAttributeType faceAttributeType = json[faces];
                if(uri) {
                    uri = false;
                } else {
                    path.append(",");
                }

                path.append(faceAttributeType);
            }

            params.put("returnFaceAttributes", path.toString());
        }

        String var12 = String.format("%s/%s", new Object[]{this.mServiceHost, "detect"});
        String var13 = WebServiceRequest.getUrl(var12, params);
        params.clear();
        params.put("url", url);
        String var14 = (String)this.mRestCall.request(var13, RequestMethod.POST, params, (String)null);
        Type var15 = (new TypeToken() {
        }).getType();
        List var16 = (List)this.mGson.fromJson(var14, var15);
        return (Face[])var16.toArray(new Face[var16.size()]);
    }

    public Face[] detect(InputStream imageStream, boolean returnFaceId, boolean returnFaceLandmarks, FaceAttributeType[] returnFaceAttributes) throws ClientException, IOException {
        HashMap params = new HashMap();
        params.put("returnFaceId", Boolean.valueOf(returnFaceId));
        params.put("returnFaceLandmarks", Boolean.valueOf(returnFaceLandmarks));
        int bytesRead;
        if(returnFaceAttributes != null && returnFaceAttributes.length > 0) {
            StringBuilder path = new StringBuilder();
            boolean uri = true;
            FaceAttributeType[] byteArrayOutputStream = returnFaceAttributes;
            bytesRead = returnFaceAttributes.length;

            for(int bytes = 0; bytes < bytesRead; ++bytes) {
                FaceAttributeType data = byteArrayOutputStream[bytes];
                if(uri) {
                    uri = false;
                } else {
                    path.append(",");
                }

                path.append(data);
            }

            params.put("returnFaceAttributes", path.toString());
        }

        String var15 = String.format("%s/%s", new Object[]{this.mServiceHost, "detect"});
        String var16 = WebServiceRequest.getUrl(var15, params);
        ByteArrayOutputStream var17 = new ByteArrayOutputStream();
        byte[] var18 = new byte[1024];

        while((bytesRead = imageStream.read(var18)) > 0) {
            var17.write(var18, 0, bytesRead);
        }

        byte[] var19 = var17.toByteArray();
        params.clear();
        params.put("data", var19);
        String json = (String)this.mRestCall.request(var16, RequestMethod.POST, params, "application/octet-stream");
        Type listType = (new TypeToken() {
        }).getType();
        List faces = (List)this.mGson.fromJson(json, listType);
        return (Face[])faces.toArray(new Face[faces.size()]);
    }

    public VerifyResult verify(UUID faceId1, UUID faceId2) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "verify"});
        params.put("faceId1", faceId1);
        params.put("faceId2", faceId2);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        return (VerifyResult)this.mGson.fromJson(json, VerifyResult.class);
    }

    public VerifyResult verify(UUID faceId, String personGroupId, UUID personId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "verify"});
        params.put("faceId", faceId);
        params.put("personGroupId", personGroupId);
        params.put("personId", personId);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        return (VerifyResult)this.mGson.fromJson(json, VerifyResult.class);
    }

    public IdentifyResult[] identity(String personGroupId, UUID[] faceIds, int maxNumOfCandidatesReturned) throws ClientException, IOException {
        return this.identity(personGroupId, faceIds, 0.5F, maxNumOfCandidatesReturned);
    }

    public IdentifyResult[] identity(String personGroupId, UUID[] faceIds, float confidenceThreshold, int maxNumOfCandidatesReturned) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "identify"});
        params.put("personGroupId", personGroupId);
        params.put("faceIds", faceIds);
        params.put("maxNumOfCandidatesReturned", Integer.valueOf(maxNumOfCandidatesReturned));
        params.put("confidenceThreshold", Float.valueOf(confidenceThreshold));
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        Type listType = (new TypeToken() {
        }).getType();
        List result = (List)this.mGson.fromJson(json, listType);
        return (IdentifyResult[])result.toArray(new IdentifyResult[result.size()]);
    }

    public void trainPersonGroup(String personGroupId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "train"});
        this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
    }

    public TrainingStatus getPersonGroupTrainingStatus(String personGroupId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "training"});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        return (TrainingStatus)this.mGson.fromJson(json, TrainingStatus.class);
    }

    public void createPersonGroup(String personGroupId, String name, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId});
        params.put("name", name);
        if(userData != null) {
            params.put("userData", userData);
        }

        this.mRestCall.request(uri, RequestMethod.PUT, params, (String)null);
    }

    public void deletePersonGroup(String personGroupId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId});
        this.mRestCall.request(uri, RequestMethod.DELETE, params, (String)null);
    }

    public void updatePersonGroup(String personGroupId, String name, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId});
        params.put("name", name);
        if(userData != null) {
            params.put("userData", userData);
        }

        this.mRestCall.request(uri, RequestMethod.PATCH, params, (String)null);
    }

    public PersonGroup getPersonGroup(String personGroupId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        return (PersonGroup)this.mGson.fromJson(json, PersonGroup.class);
    }

    public PersonGroup[] getPersonGroups() throws ClientException, IOException {
        return this.listPersonGroups("", 1000);
    }

    public PersonGroup[] listPersonGroups(String start, int top) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s?start=%s&top=%s", new Object[]{this.mServiceHost, "persongroups", start, Integer.valueOf(top)});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        Type listType = (new TypeToken() {
        }).getType();
        List result = (List)this.mGson.fromJson(json, listType);
        return (PersonGroup[])result.toArray(new PersonGroup[result.size()]);
    }

    public PersonGroup[] listPersonGroups(String start) throws ClientException, IOException {
        return this.listPersonGroups(start, 1000);
    }

    public PersonGroup[] listPersonGroups(int top) throws ClientException, IOException {
        return this.listPersonGroups("", top);
    }

    public PersonGroup[] listPersonGroups() throws ClientException, IOException {
        return this.listPersonGroups("", 1000);
    }

    public CreatePersonResult createPerson(String personGroupId, String name, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons"});
        params.put("name", name);
        if(userData != null) {
            params.put("userData", userData);
        }

        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        return (CreatePersonResult)this.mGson.fromJson(json, CreatePersonResult.class);
    }

    public Person getPerson(String personGroupId, UUID personId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId.toString()});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        return (Person)this.mGson.fromJson(json, Person.class);
    }

    /** @deprecated */
    @Deprecated
    public Person[] getPersons(String personGroupId) throws ClientException, IOException {
        return this.listPersons(personGroupId, "", 1000);
    }

    public Person[] listPersons(String personGroupId, String start, int top) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s?start=%s&top=%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", start, Integer.valueOf(top)});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        Type listType = (new TypeToken() {
        }).getType();
        List result = (List)this.mGson.fromJson(json, listType);
        return (Person[])result.toArray(new Person[result.size()]);
    }

    public Person[] listPersons(String personGroupId, String start) throws ClientException, IOException {
        return this.listPersons(personGroupId, start, 1000);
    }

    public Person[] listPersons(String personGroupId, int top) throws ClientException, IOException {
        return this.listPersons(personGroupId, "", top);
    }

    public Person[] listPersons(String personGroupId) throws ClientException, IOException {
        return this.listPersons(personGroupId, "", 1000);
    }

    public AddPersistedFaceResult addPersonFace(String personGroupId, UUID personId, String url, String userData, FaceRectangle targetFace) throws ClientException, IOException {
        HashMap params = new HashMap();
        String path = String.format("%s/%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId, "persistedfaces"});
        if(userData != null && userData.length() > 0) {
            params.put("userData", userData);
        }

        String uri;
        if(targetFace != null) {
            uri = String.format(Locale.ENGLISH, "%1d,%2d,%3d,%4d", new Object[]{Integer.valueOf(targetFace.left), Integer.valueOf(targetFace.top), Integer.valueOf(targetFace.width), Integer.valueOf(targetFace.height)});
            params.put("targetFace", uri);
        }

        uri = WebServiceRequest.getUrl(path, params);
        params.clear();
        params.put("url", url);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        return (AddPersistedFaceResult)this.mGson.fromJson(json, AddPersistedFaceResult.class);
    }

    public AddPersistedFaceResult addPersonFace(String personGroupId, UUID personId, InputStream imageStream, String userData, FaceRectangle targetFace) throws ClientException, IOException {
        HashMap params = new HashMap();
        String path = String.format("%s/%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId, "persistedfaces"});
        if(userData != null && userData.length() > 0) {
            params.put("userData", userData);
        }

        String uri;
        if(targetFace != null) {
            uri = String.format(Locale.ENGLISH, "%1d,%2d,%3d,%4d", new Object[]{Integer.valueOf(targetFace.left), Integer.valueOf(targetFace.top), Integer.valueOf(targetFace.width), Integer.valueOf(targetFace.height)});
            params.put("targetFace", uri);
        }

        uri = WebServiceRequest.getUrl(path, params);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        int bytesRead;
        while((bytesRead = imageStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, bytesRead);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        params.clear();
        params.put("data", data);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, "application/octet-stream");
        return (AddPersistedFaceResult)this.mGson.fromJson(json, AddPersistedFaceResult.class);
    }

    public PersonFace getPersonFace(String personGroupId, UUID personId, UUID persistedFaceId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId, "persistedfaces", persistedFaceId});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        return (PersonFace)this.mGson.fromJson(json, PersonFace.class);
    }

    public void updatePersonFace(String personGroupId, UUID personId, UUID persistedFaceId, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        if(userData != null) {
            params.put("userData", userData);
        }

        String uri = String.format("%s/%s/%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId, "persistedfaces", persistedFaceId});
        this.mRestCall.request(uri, RequestMethod.PATCH, params, (String)null);
    }

    public void updatePerson(String personGroupId, UUID personId, String name, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        if(name != null) {
            params.put("name", name);
        }

        if(userData != null) {
            params.put("userData", userData);
        }

        String uri = String.format("%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId});
        this.mRestCall.request(uri, RequestMethod.PATCH, params, (String)null);
    }

    public void deletePerson(String personGroupId, UUID personId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId});
        this.mRestCall.request(uri, RequestMethod.DELETE, params, (String)null);
    }

    public void deletePersonFace(String personGroupId, UUID personId, UUID persistedFaceId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "persongroups", personGroupId, "persons", personId, "persistedfaces", persistedFaceId});
        this.mRestCall.request(uri, RequestMethod.DELETE, params, (String)null);
    }

    public SimilarFace[] findSimilar(UUID faceId, UUID[] faceIds, int maxNumOfCandidatesReturned) throws ClientException, IOException {
        return this.findSimilar(faceId, faceIds, maxNumOfCandidatesReturned, FindSimilarMatchMode.matchPerson);
    }

    public SimilarFace[] findSimilar(UUID faceId, UUID[] faceIds, int maxNumOfCandidatesReturned, FindSimilarMatchMode mode) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "findsimilars"});
        params.put("faceId", faceId);
        params.put("faceIds", faceIds);
        params.put("maxNumOfCandidatesReturned", Integer.valueOf(maxNumOfCandidatesReturned));
        params.put("mode", mode.toString());
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        Type listType = (new TypeToken() {
        }).getType();
        List result = (List)this.mGson.fromJson(json, listType);
        return (SimilarFace[])result.toArray(new SimilarFace[result.size()]);
    }

    public SimilarPersistedFace[] findSimilar(UUID faceId, String faceListId, int maxNumOfCandidatesReturned) throws ClientException, IOException {
        return this.findSimilar(faceId, faceListId, maxNumOfCandidatesReturned, FindSimilarMatchMode.matchPerson);
    }

    public SimilarPersistedFace[] findSimilar(UUID faceId, String faceListId, int maxNumOfCandidatesReturned, FindSimilarMatchMode mode) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "findsimilars"});
        params.put("faceId", faceId);
        params.put("faceListId", faceListId);
        params.put("maxNumOfCandidatesReturned", Integer.valueOf(maxNumOfCandidatesReturned));
        params.put("mode", mode.toString());
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        Type listType = (new TypeToken() {
        }).getType();
        List result = (List)this.mGson.fromJson(json, listType);
        return (SimilarPersistedFace[])result.toArray(new SimilarPersistedFace[result.size()]);
    }

    public GroupResult group(UUID[] faceIds) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "group"});
        params.put("faceIds", faceIds);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        return (GroupResult)this.mGson.fromJson(json, GroupResult.class);
    }

    public void createFaceList(String faceListId, String name, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId});
        params.put("name", name);
        params.put("userData", userData);
        this.mRestCall.request(uri, RequestMethod.PUT, params, (String)null);
    }

    public FaceList getFaceList(String faceListId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        return (FaceList)this.mGson.fromJson(json, FaceList.class);
    }

    public FaceListMetadata[] listFaceLists() throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s", new Object[]{this.mServiceHost, "facelists"});
        String json = (String)this.mRestCall.request(uri, RequestMethod.GET, params, (String)null);
        Type listType = (new TypeToken() {
        }).getType();
        List result = (List)this.mGson.fromJson(json, listType);
        return (FaceListMetadata[])result.toArray(new FaceListMetadata[result.size()]);
    }

    public void updateFaceList(String faceListId, String name, String userData) throws ClientException, IOException {
        HashMap params = new HashMap();
        if(name != null) {
            params.put("name", name);
        }

        if(userData != null) {
            params.put("userData", userData);
        }

        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId});
        this.mRestCall.request(uri, RequestMethod.PATCH, params, (String)null);
    }

    public void deleteFaceList(String faceListId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId});
        this.mRestCall.request(uri, RequestMethod.DELETE, params, (String)null);
    }

    public AddPersistedFaceResult addFacesToFaceList(String faceListId, String url, String userData, FaceRectangle targetFace) throws ClientException, IOException {
        HashMap params = new HashMap();
        String path = String.format("%s/%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId, "persistedfaces"});
        if(userData != null && userData.length() > 0) {
            params.put("userData", userData);
        }

        String uri;
        if(targetFace != null) {
            uri = String.format(Locale.ENGLISH, "%1d,%2d,%3d,%4d", new Object[]{Integer.valueOf(targetFace.left), Integer.valueOf(targetFace.top), Integer.valueOf(targetFace.width), Integer.valueOf(targetFace.height)});
            params.put("targetFace", uri);
        }

        uri = WebServiceRequest.getUrl(path, params);
        params.clear();
        params.put("url", url);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, (String)null);
        return (AddPersistedFaceResult)this.mGson.fromJson(json, AddPersistedFaceResult.class);
    }

    public AddPersistedFaceResult AddFaceToFaceList(String faceListId, InputStream imageStream, String userData, FaceRectangle targetFace) throws ClientException, IOException {
        HashMap params = new HashMap();
        String path = String.format("%s/%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId, "persistedfaces"});
        if(userData != null && userData.length() > 0) {
            params.put("userData", userData);
        }

        String uri;
        if(targetFace != null) {
            uri = String.format(Locale.ENGLISH, "%1d,%2d,%3d,%4d", new Object[]{Integer.valueOf(targetFace.left), Integer.valueOf(targetFace.top), Integer.valueOf(targetFace.width), Integer.valueOf(targetFace.height)});
            params.put("targetFace", uri);
        }

        uri = WebServiceRequest.getUrl(path, params);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        int bytesRead;
        while((bytesRead = imageStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, bytesRead);
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        params.clear();
        params.put("data", data);
        String json = (String)this.mRestCall.request(uri, RequestMethod.POST, params, "application/octet-stream");
        return (AddPersistedFaceResult)this.mGson.fromJson(json, AddPersistedFaceResult.class);
    }

    public void deleteFacesFromFaceList(String faceListId, UUID persistedFaceId) throws ClientException, IOException {
        HashMap params = new HashMap();
        String uri = String.format("%s/%s/%s/%s/%s", new Object[]{this.mServiceHost, "facelists", faceListId, "persistedfaces", persistedFaceId});
        this.mRestCall.request(uri, RequestMethod.DELETE, params, (String)null);
    }

    public static enum FindSimilarMatchMode {
        matchPerson {
            public String toString() {
                return "matchPerson";
            }
        },
        matchFace {
            public String toString() {
                return "matchFace";
            }
        };

        private FindSimilarMatchMode() {
        }
    }

    public static enum FaceAttributeType {
        Age {
            public String toString() {
                return "age";
            }
        },
        Gender {
            public String toString() {
                return "gender";
            }
        },
        FacialHair {
            public String toString() {
                return "facialHair";
            }
        },
        Smile {
            public String toString() {
                return "smile";
            }
        },
        HeadPose {
            public String toString() {
                return "headPose";
            }
        },
        Glasses {
            public String toString() {
                return "glasses";
            }
        },
        Emotion {
            public String toString() {
                return "emotion";
            }
        },
        Hair {
            public String toString() {
                return "hair";
            }
        },
        Makeup {
            public String toString() {
                return "makeup";
            }
        },
        Occlusion {
            public String toString() {
                return "occlusion";
            }
        },
        Accessories {
            public String toString() {
                return "accessories";
            }
        },
        Noise {
            public String toString() {
                return "noise";
            }
        },
        Exposure {
            public String toString() {
                return "exposure";
            }
        },
        Blur {
            public String toString() {
                return "blur";
            }
        };

        private FaceAttributeType() {
        }
    }
}
