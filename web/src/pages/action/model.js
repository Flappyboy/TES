import { message } from 'antd';
import { fakeSubmitForm, allActions,im } from './service';
import {queryAllInfoTypes} from "@/pages/info/service";

const Model = {
  namespace: 'action',
  state: {
    actions:[],
    action: {
      "name": "ImportData",
      "action": "ImportDataAction",
      "desc": "导入数据",
      "meta": [
        {
          "name": "file",
          "displayName": "File",
          "inputClass": "file",
        }
      ],
    },
  },
  effects: {
    *submitAdvancedForm({ payload }, { call }) {
      yield call(fakeSubmitForm, payload);
      message.success('提交成功');
    },
    *execute({ payload }, { call }){
      const response = yield call(im, payload);
      console.log(response)
    },
    *fetchActions({ payload }, { call, put }){
      console.log('fetchActionssss');
      const response = yield call(allActions, payload);
      yield put({
        type: 'saveActions',
        payload: {
          list: response.result,
          pagination: {
            pageSize: 5,
          },
        },
      });
    },
    *chooseAction({ payload }, { call }){

    }
  },
  reducers: {
    saveActions(state, action) {
      return {...state, actions: action.payload};
    },
  }
};
export default Model;
