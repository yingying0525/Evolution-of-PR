package Tools.JsonUtil;

/**
 * Created by hp on 2014/3/28.
 */

import Tools.FileUtil;
import Tools.Statistic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonMain
{
    public static void main(String[] args)
    {
        JsonRead jR = new JsonRead();
        JsonWrite jW = new JsonWrite();
        /*JSONObject obj = jR.readFromFile("read.json");
        try
        {
            //json的object 利用 getJsonObject和getJsonArray两个函数得到所需对象
            //getString函数可以得到对像的value
            System.out.println(obj.getJSONObject("section").getJSONArray("signing").toString());
            System.out.println(obj.getJSONObject("section").getString("title"));
            System.out.println(obj.toString());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }*/




        //JsonMain test = new JsonMain();
        //test.jsonGenerate();

    }
    public Map<String, String> ReadHotPeopleList(String file)
    {
        Map<String, String> map = new HashMap<String, String>();
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        String time, line;
        try
        {
            while ((time = br.readLine()) != null)
            {
                line = br.readLine();
                map.put(time, line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }
    public JSONObject ReadFileToJson(String file, Map<String, Integer> map, int pid)
    {
        JSONObject json = new JSONObject();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String time, line;
            JSONArray jsonArray = new JSONArray();
            while ((time = br.readLine()) != null)
            {
                line = br.readLine();
                line = line.replaceAll("\\[", "");
                line = line.replaceAll("\\]", "");
                if (line.length() == 0)
                    continue;
                String [] strs = line.split(", ");
                for (int i = 0; i < strs.length; i ++)
                {
                    String []subs = strs[i].split("\t");
                    int id = map.get(subs[1]);
                    double sim = Double.parseDouble(subs[2]);
                    JSONObject jsonSub = jsonGenerate(id, pid, subs[1], sim);
                    jsonArray.put(jsonSub);
                }
            }
            json.put("人物关系", jsonArray);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }
    public JSONObject MapToJson(Map<String, String> map_num, Map<String, String> map_rela, Map<String, Integer> map_people, String name, int pid)
    {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (String sn: map_num.keySet())
        {
            JSONObject jsonSub = jsonGenerate(sn, map_num.get(sn), map_rela.get(sn), map_people, pid);
            try
            {
                json2.put(sn,jsonSub);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            json.put("name",name);
            json.put("id", pid);
            json.put("data", json2);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return json;
    }
    public JSONObject MapToJson(Map<String, String> map_num, Map<String, Integer> map_people, String name, int pid)
    {
        JSONObject json = new JSONObject();
        JSONObject json2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List <String> a = new ArrayList<String>();
        a.addAll(map_num.keySet());
        Collections.sort(a);
        System.out.println(a.get(0));
        for (int j = 0; j < a.size(); j ++)
        {
            String sn = a.get(j);
            //JSONObject jsonSub = jsonGenerate_num(sn, map_num.get(sn));
            String num = map_num.get(sn);
            num = num.replaceAll("\\[", "");
            num = num.replaceAll("\\]", "");
            String []strs = num.split(", ");
            int count = 0;
            for (int i = 0; i < 31; i++)
            {
                count += Integer.parseInt(strs[i]);
            }
            try
            {
                json2.put(sn, count);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            json.put("name",name);
            json.put("id", pid);
            json.put("number", json2);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return json;
    }
    public Map<String, String> ReadFileToMap(String file)
    {
        Map<String, String> map = new HashMap<String, String>();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line_date, line_cont;
            while ((line_date = br.readLine()) != null)
            {
                line_cont = br.readLine();
                map.put(line_date, line_cont);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return map;
    }
    public Map<String, Integer> ReadPeopleFileToMap(String file)
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null)
            {
                if (!map.containsKey(line))
                    map.put(line, map.size() + 1);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return map;
    }

    public JSONObject jsonGenerate(int id, int pid, String name, double sim)
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("id", id).put("pid", pid).put("name", name).put("similarity", sim);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return json;
    }
    public JSONObject jsonGenerate(String s, String num, String rela, Map<String, Integer> map_people, int pid)
    {
        JSONObject json1 = new JSONObject();
        JSONObject json2 = new JSONObject();
        try
        {
            JSONObject j1 = jsonGenerate_num(s, num);
            JSONArray j2 = jsonGenerate_rela(rela, map_people, pid);
            json1.put("number", j1).put("relation", j2);
            json2.put(s,json1);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return json1;
    }
    public JSONObject jsonGenerate_num(String s, String num)
    {
        JSONObject json1 = new JSONObject();
        JSONObject json2 = new JSONObject();
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        int day = 0;
        try
        {
            Date date = df.parse(s);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        num = num.replaceAll("\\[", "");
        num = num.replaceAll("\\]", "");
        String []strs = num.split(", ");
        System.out.println(s);
        for (int i = 1; i <= day ; i++)
        {
            try
            {
                json1.put(i+"",strs[i-1]);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return json1;
    }
    public JSONArray jsonGenerate_rela( String rela, Map<String, Integer> map_people, int pid)
    {
        JSONArray jsonArray = new JSONArray();
        rela = rela.replaceAll("\\[", "");
        rela = rela.replaceAll("\\]", "");
        if (rela.length() == 0)
        {
            return jsonArray;
        }
        String [] strs = rela.split(", ");
        for (int i = 0; i < strs.length; i ++)
        {
            String []subs = strs[i].split("\t");
            int id = map_people.get(subs[1]);
            double sim = Double.parseDouble(subs[2]);
            JSONObject jsonSub = jsonGenerate(id, pid, subs[1], sim);
            jsonArray.put(jsonSub);
        }

        return jsonArray;
    }
    public JSONObject jsonGenerate()
    {
        JSONObject json1 = new JSONObject();
        JSONObject json2 = new JSONObject();
        JSONObject json3 = new JSONObject();
        JSONObject json4 = new JSONObject();
        JSONObject json5 = new JSONObject();
        JSONObject json6 = new JSONObject();
        JSONObject json7 = new JSONObject();
        JSONObject json8 = new JSONObject();
        JSONArray jsonA1 = new JSONArray();
        JSONArray jsonA2 = new JSONArray();
        JSONArray jsonA3 = new JSONArray();
        try
        {
            json2.put("id", "427169").put("name", "张高丽").put("sim", "0.7894").put("pid", "190923");
            json3.put("id", "427169").put("name", "张高丽").put("sim", "0.7894").put("pid", "190923");
            json4.put("id", "427169").put("name", "张高丽").put("sim", "0.7894").put("pid", "190923");
            jsonA2.put(json2).put(json3).put(json4);
            json1.put("id", "190923").put("name", "李克强").put("relation", jsonA2);

            json5.put("id", "427169").put("name", "张高丽").put("sim", "0.7894").put("pid", "190923");
            json6.put("id", "427169").put("name", "张高丽").put("sim", "0.7894").put("pid", "190923");
            json7.put("id", "427169").put("name", "张高丽").put("sim", "0.7894").put("pid", "190923");
            jsonA3.put(json5).put(json6).put(json7);
            json8.put("id", "190923").put("name", "习近平").put("relation", jsonA3);

            jsonA1.put(json1).put(json8);
            JsonWrite jW = new JsonWrite();
            jW.writeToFile(jsonA1, "D:\\datadd\\xiaoke\\renwu\\test");

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json8;
    }
}
