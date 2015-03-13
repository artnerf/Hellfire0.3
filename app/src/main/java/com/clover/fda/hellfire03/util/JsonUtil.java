package com.clover.fda.hellfire03.util;

import com.clover.fda.hellfire03.menu.MenuElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by firstdata on 12.03.15.
 */
public class JsonUtil {
    public static JSONObject menuElementToJSon(MenuElement menu) {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();

            jsonObj.put("msg_text_id",      menu.msg_text_id);
            jsonObj.put("msg_text",         menu.msg_text);
            jsonObj.put("hidden",           menu.hidden);
            jsonObj.put("menu_item_id",     menu.menu_item_id);
            jsonObj.put("item_property",    menu.item_property);
            jsonObj.put("number",           menu.number);
            jsonObj.put("pre_param_id",     menu.pre_param_id);
            jsonObj.put("pw_level",         menu.pw_level);


            // and finally we add the sub menus
            JSONArray jsonArr = new JSONArray();

            for (MenuElement submenu : menu.submenu) {
                JSONObject pnObj = menuElementToJSon(submenu);
                jsonArr.put(pnObj);
            }
            jsonObj.put("submenu", jsonArr);

            return jsonObj;

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static MenuElement jsonToMenuElement(JSONObject jsonObj, MenuElement previous) {
        try {
            // Here we convert JSON to Java Object
            MenuElement menu = new MenuElement();

            menu.msg_text_id    = jsonObj.getLong   ("msg_text_id");
            menu.msg_text       = jsonObj.getString ("msg_text");
            menu.hidden         = jsonObj.getLong   ("hidden");
            menu.menu_item_id   = jsonObj.getLong   ("menu_item_id");
            menu.item_property  = jsonObj.getLong   ("item_property");
            menu.number         = jsonObj.getLong   ("number");
            menu.pre_param_id   = jsonObj.getLong   ("pre_param_id");
            menu.pw_level       = jsonObj.getLong   ("pw_level");
            menu.previousMenuElement = previous;

            // and finally we load the sub menus
            JSONArray jsonArr = jsonObj.getJSONArray("submenu");

            for(int i=0; i < jsonArr.length(); i++){
                JSONObject json_submenu = jsonArr.getJSONObject(i);
                MenuElement menu_submenu = jsonToMenuElement(json_submenu, menu);
                if (menu_submenu != null) {
                    menu.submenu.add(menu_submenu);
                }
            }

            return menu;

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
