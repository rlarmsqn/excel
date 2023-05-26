package com.jbt.water;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import com.google.api.services.drive.model.File;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
@Slf4j
public class GoogleDrive {
    private static final String APPLICATION_NAME = "GoogleDriveApiTestName";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES =
            Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";


    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        GoogleDrive.upload();
//        GoogleDrive.download();
//        GoogleDrive.update();
//        GoogleDrive.delete();
//        GoogleDrive.driveChangeFileLog(GoogleDrive.fetchStartPageToken());
//        GoogleDrive.push();
    }
    public static String upload() throws IOException, GeneralSecurityException {

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Build a new authorized API client service.
        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName("GoogleDriveApiTestName")
                .build();
        // Upload file photo.jpg on drive.
        File fileMetadata = new File();
//        fileMetadata.setName("NN_SCN1g02");
        fileMetadata.setName("ttttt");
        // File's content.
//        java.io.File filePath = new java.io.File("C:\\Users\\srmsq\\Desktop\\waterdata\\NN_SCN1.g02.hdf");
        java.io.File filePath = new java.io.File("C:\\Users\\srmsq\\Desktop\\water\\aaaaaaa.txt");
        // Specify media type and file-path for file.
        FileContent mediaContent = new FileContent("*/*", filePath);
        try {
            File file = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            System.out.println("File ID: " + file.getId());
            return file.getId();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to upload file: " + e.getDetails());
            throw e;
        }
    }

    public static void download(String fileId, String fileName) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        service.files().get(getFileId(fileId)).executeMediaAndDownloadTo(outputStream);

        FileOutputStream fos = new FileOutputStream(fileName + ".hdf");
        outputStream.writeTo(fos);
        System.out.println(outputStream);*/

        // txt 파일
        HttpResponse response = service.files().get(fileId).executeMedia();
        OutputStream outputStream = new FileOutputStream(fileName + ".txt");
        response.download(outputStream);


    }

    public static void update() throws IOException, GeneralSecurityException{
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // 파일명은 업데이트 파일 명으로 바뀜
        Path path = Paths.get("C:\\Users\\srmsq\\Desktop\\water\\텟텟스트.txt");
        File fileMetadata = new File();
        fileMetadata.setName(path.getFileName().toString());
        AbstractInputStreamContent mediaContent = new FileContent(null, path.toFile());
        service.files().update(getFileId("텟스트"), fileMetadata, mediaContent).execute();
    }

    public static void delete() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        service.files().delete(getFileId("test")).execute();
    }

    public static String fetchStartPageToken() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        try {
            StartPageToken response = service.changes()
                    .getStartPageToken().execute();
//            System.out.println("Start token: " + response.getStartPageToken());

            return response.getStartPageToken();
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to fetch start page token: " + e.getDetails());
            throw e;
        }
    }

    // 파일 변경 사항 (업로드 or 변경 시)
    public static String driveChangeFileLog(String startPageToken) throws GeneralSecurityException, IOException {
        log.info("드라이브체인지로그");
        String pageToken = null;
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        try {
            /*com.google.api.services.drive.model.ChangeList changes = service
                    .changes()
                    .list("1")
                    .execute();

            for (com.google.api.services.drive.model.Change change : changes.getChanges()) {
                if (change.getRemoved() != null && change.getRemoved()) {
                    // 변경 사항이 삭제된 경우, 스킵합니다.
                    continue;
                }

                if (change.getFile() != null) {
                    // 변경 사항에 포함된 파일 정보 가져오기
                    String fileId = change.getFileId();
                    File file = change.getFile();
                    String fileName = file.getName();

                    if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                        // 변경 사항이 폴더인 경우 스킵합니다.
                        continue;
                    }

                    // 드래그 앤 드롭으로 추가된 파일 출력
//                    log.info("File ID: " + fileId + ", File Name: " + fileName);

                    // 파일 상세 정보 가져오기 (옵션)
                    File fullFile = service.files().get(fileId).execute();
                    log.info(change.getFile().toPrettyString());
                    // fullFile 변수를 사용하여 파일의 상세 정보에 접근할 수 있습니다.
                }
            }*/
//            while (startPageToken != null) {
//                String token = "1";

                ChangeList changes = service.changes().list( String.valueOf(Integer.parseInt(startPageToken) - 1))
//                        .setIncludeItemsFromAllDrives(true) // 모든 드라이버 항목 포함 설정
                        .execute();
                for (com.google.api.services.drive.model.Change change : changes.getChanges()) {
                    // Process change
                    // removed 값이 false 인것 / 변경 시간이 당일 날짜인것만 출력 / 변경, 신규업로드(둘다 당일 변경목록에 추가됨 구분x) 근데 api로 등록하는것만 변경목록에 추가됨
                    if(!change.getRemoved()) {
                        if(LocalDate.now().toString().equals(change.getTime().toString().substring(0, 10))) {
//                        System.out.println("신규 업로드 / 변경 파일 시 다운로드할 FILE ID : " + change.getFileId());
                            download(change.getFileId(), change.getFile().getName());
                            log.info(change.toPrettyString());
//                            log.info("------------");
                        }
                    }
                }

                startPageToken = changes.getNextPageToken();
//                log.info("startPageToken : " + startPageToken);
//            }

            return startPageToken;
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            System.err.println("Unable to fetch changes: " + e.getDetails());
            throw e;
        }
    }

    // 푸시 알림
    public static void push() throws IOException, GeneralSecurityException {
        log.info("push-----------");
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Channel channel = new Channel();
        channel.setId(UUID.randomUUID().toString());
        channel.setType("web_hook");
        channel.setAddress("https://boooddha.com/notifications");
        // pageToken root면 드라이브내 전체 파일을 뜻함
        Channel creatChannel = service.changes().watch("1", channel).execute();

        log.info("구독 ID : " + creatChannel.getId());

        /*HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost("https://www.googleapis.com/drive/v3/changes/watch");
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Connection", "keep-alive");
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.addHeader("Authorization", GoogleDrive.getCredentials(HTTP_TRANSPORT).getAccessToken());

        org.apache.http.HttpResponse response = client.execute(postRequest);

        System.out.println(GoogleDrive.getCredentials(HTTP_TRANSPORT).getAccessToken());

        if (response.getStatusLine().getStatusCode() == 200) {
            ResponseHandler<String> handler = new BasicResponseHandler();
            String body = handler.handleResponse(response);
//            System.out.println(body);
        } else {
//            System.out.println("response is error : " + response.getStatusLine().getStatusCode());
        }*/
    }

    public static String getFileId(String getId) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Drive service = new Drive.Builder(new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                GoogleDrive.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String id = "";

        FileList result = service.files().list().execute();
        List<File> files = result.getFiles();
        if (files == null || files.isEmpty()) {
            System.out.println("No files found.");
        } else {
            for (File file : files) {
                if(getId.equals(file.getName())) {
                    id = file.getId();
                }
            }
        }

        return id;
    }

    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

//        // 이 도메인으로 인증창을 띄위려하면 Address already in use 에러 뜸..
        LocalServerReceiver receiverBuilder = new LocalServerReceiver.Builder()
//                .setHost(InetAddress.getLocalHost().getHostAddress());
                .setHost("www.boooddha.com")
                .setPort(443)
                .build();


        Credential credential = new AuthorizationCodeInstalledApp(flow, receiverBuilder).authorize("user");


       /* AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                HTTP_TRANSPORT,
                JSON_FACTORY,
                new com.google.api.client.http.GenericUrl("https://accounts.google.com/o/oauth2/token"),
                new com.google.api.client.auth.oauth2.ClientParametersAuthentication("440723426577-m14q8nibtbj0n4n10n5ittud2feugprh.apps.googleusercontent.com", "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"),
                "440723426577-m14q8nibtbj0n4n10n5ittud2feugprh.apps.googleusercontent.com",
                "https://accounts.google.com/o/oauth2/auth")
                .setScopes(SCOPES)
                .build();


        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
        String url = authorizationUrl.setRedirectUri("https://www.boooddha.com").build();

        AuthorizationCodeInstalledApp.browse(url);
        System.out.print("Enter the authorization code: ");
        Scanner scanner = new Scanner(System.in);
        String authorizationCode = scanner.nextLine();
*/
        // 액세스 토큰을 요청합니다.
//        TokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
//                .setRedirectUri("https://www.boooddha.com")
//                .execute();

        // Credential을 생성합니다.
//        Credential credential = flow.createAndStoreCredential(tokenResponse, null);

        return credential;
    }
}