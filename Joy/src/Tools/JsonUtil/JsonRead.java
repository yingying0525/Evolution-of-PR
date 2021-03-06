package Tools.JsonUtil;

/**
 * Created by hp on 2014/3/28.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonRead
{
    public JSONObject readObjectFromFile(String fileName)
    {
        JSONObject readObj = new JSONObject();
        try {
            readObj = new JSONObject(new JSONTokener(new FileReader(new File(fileName))));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  readObj;
    }
    public JSONArray readArrayFromFile(String fileName)
    {
        JSONArray readArr = new JSONArray();
        try {
            readArr = new JSONArray(new JSONTokener(new FileReader(new File(fileName))));


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  readArr;
    }
}
