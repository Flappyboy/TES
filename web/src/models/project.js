import request from '@/utils/request';

const ProjectModel = {
  namespace: 'project',
  state: {
    currentProjectde: {
      "id": 1,
      "createdTime": 1575216488269,
      "updatedTime": 1575216488269,
      "name": "CT",
      "desc": "just test"
    },
    currentProject: null,
  },
  effects: {
    *chooseProject(payload, { call, put }) {
      // const response = yield call(queryCurrent);
      yield put({
        type: 'chooseProject',
        payload: payload,
      });
    },
    *init(call, put){
      const response = yield call(request('/api/project',{
        pageNum: 1,
        pageSize: 1,
      }));
      yield put({
        type: 'chooseProject',
        payload: response.result[0],
      });
    }
  },
  reducers: {
    changeProject(state, action) {
      const project = action.payload;
      if (project.id && state.currentProject.id!==project.id){
        console.log("choose"+project.id);
        return { ...state, currentProject: project || {} };
      }
      return { ...state }
    },
  },
};
export default ProjectModel;
