import { Avatar, Icon, Menu, Spin } from 'antd';
import { FormattedMessage } from 'umi-plugin-react/locale';
import React from 'react';
import router from 'umi/router';
import { connect } from 'dva';

class CurrentProject extends React.Component {

  toChoose(){
    router.push(`/project`);
  }

  renderProject(){
    const {
      currentProject
    } = this.props;
    /*console.log("show ")
    console.log(currentProject)*/
    return currentProject && currentProject.name ? (
        currentProject.name
      ) : (
        '~'
      )
    ;
  }

  render() {
    return (
      <a onClick={this.toChoose}>
        当前项目：
        {this.renderProject()}
      </a>
    )
  }
}

export default connect(({ project }) => ({
  currentProject: project.currentProject,
}))(CurrentProject);
