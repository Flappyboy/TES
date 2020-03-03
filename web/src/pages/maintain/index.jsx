import { Button, Card, Icon, List, Typography } from 'antd';
import React, { Component } from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { connect } from 'dva';
// 引入 ECharts 主模块
import echarts from 'echarts';
/*// 引入柱状图
import  'echarts/lib/chart/bar';
// 引入提示框和标题组件
import 'echarts/lib/component/tooltip';
import 'echarts/lib/component/title';*/
import styles from './style.less';
import { queryMaintain } from './service';

const { Paragraph } = Typography;

@connect(({ maintain, loading }) => ({
  maintain,
  loading: loading.models.maintain,
}))
class Maintain extends Component {
  componentDidMount() {
    this.show();
  }

  show() {
    queryMaintain(1)
      .then(function (response) {
      console.log(response);
    });
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('maintain-chart'));
    // 绘制图表
    myChart.setOption({
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        type: 'line'
      }]
    })
  }
  render() {
    return (
      <PageHeaderWrapper>
        <div id="maintain-chart" style={{width: '80%',height:400}}></div>
      </PageHeaderWrapper>
    );
  }
}

export default Maintain;
