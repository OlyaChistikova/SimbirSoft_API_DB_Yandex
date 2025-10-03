package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static helpers.BaseRequests.*;

public class DeleteFolderTest extends BaseTest{
    private String folderName;

    @BeforeMethod
    public void createPostForDelete() {
        folderName = "Удаляемая папка";
        createFolderRequest(folderName);
    }

    @AfterClass
    public void cleanTrash(){
        cleanTrashResourcesAfterDelete();
    }

    @Test
    public void deleteCorrectFolderWithAuthTest() {
        Response responseDeleteFolder = deleteFolderAuthRequest(folderName);

        Assert.assertTrue(responseDeleteFolder.getBody().asString().isEmpty());

        List<String> listFolders = BaseRequests.getAllNamesFoldersRequest();

        Assert.assertFalse(listFolders.contains(folderName));
    }

    @Test
    public void deleteRemovedFolderWithAuthTest() {
        deleteFolderRequest(folderName);

        Response responseGetFolder = getUnExistentFolderAuthRequest(folderName);

        Assert.assertNotNull(responseGetFolder.path("error"));
        Assert.assertNotNull(responseGetFolder.path("description"));
        Assert.assertNotNull(responseGetFolder.path("message"));

        Response responseDeleteFolder = deleteAlreadyDeletedFolderAuthRequest(folderName);

        Assert.assertNotNull(responseDeleteFolder.path("error"));
        Assert.assertNotNull(responseDeleteFolder.path("description"));
        Assert.assertNotNull(responseDeleteFolder.path("message"));
    }

    @Test
    public void deleteFolderWithoutAuthTest() {
        Response responseDeleteFolder = deleteFolderWithoutAuthRequest(folderName);

        Assert.assertNotNull(responseDeleteFolder.path("error"));
        Assert.assertNotNull(responseDeleteFolder.path("description"));
        Assert.assertNotNull(responseDeleteFolder.path("message"));

        List<String> listFolders = BaseRequests.getAllNamesFoldersRequest();

        Assert.assertTrue(listFolders.contains(folderName));

        deleteFolderAuthRequest(folderName);
    }
}