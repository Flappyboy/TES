package top.jach.tes.core.api.domain.action;

import com.alibaba.fastjson.JSONObject;

public interface StatefulAction {
    String serialize();

    void deserialization(String action);

    static String serializeActionToJson(Action action){
        JSONObject jsonObject = new JSONObject();
        if(action!=null) {
            jsonObject.put("class", action.getClass().getName());
            if (action instanceof StatefulAction) {
                StatefulAction statefulAction = (StatefulAction) action;
                jsonObject.put("attr", statefulAction.serialize());
            }
        }
        return jsonObject.toJSONString();
    }
    static Action deserializeActionFromJson(String json){
        JSONObject jsonObject = JSONObject.parseObject(json);
        try {
            Class clazz = Class.forName(jsonObject.getJSONObject("attr").getString("class"));
            Action action = (Action) clazz.newInstance();
            if(action instanceof StatefulAction){
                StatefulAction statefulAction = (StatefulAction) action;
                statefulAction.deserialization(jsonObject.getString("attr"));
            }
            return action;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
