import { Form, Row, Col, Select, Input, Button, Card, Icon, List, Typography, Table } from 'antd';
import React, { Component } from 'react';
// import React, { Component, Fragment } from 'react';
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
import Link from '../../dashboard/workplace';

const { Paragraph } = Typography;
const FormItem = Form.Item;
var { versionNames } = [];

@connect(({ metrics, loading }) => ({
  metrics,
  loading: loading.models.metrics,
}))
class Metrics extends Component {
  componentDidMount() {
    this.show();
  }

  show() {
    queryMaintain(1).then(function(response) {
      console.log(response);
      versionNames = response.datasource;
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(document.getElementById('maintain-chart'));

      // 绘制图表
      myChart.setOption({
        xAxis: {
          type: 'category',
          data: response.x,
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: response.y,
            type: 'line',
          },
        ],
      });
    });
  }

  handleSearch = e => {
    e.preventDefault();
    const { dispatch, form } = this.props;
  };

  renderForm() {
    const { form } = this.props;
    const { getFieldDecorator } = form;
    return (
      <Form onSubmit={this.handleSearch} layout="inline">
        <Row
          gutter={{
            md: 8,
            lg: 24,
            xl: 48,
          }}
        >
          <Col md={8} sm={24}>
            <FormItem label="微服务名称">
              {getFieldDecorator('microservice_name')(
                <Select
                  placeholder="请选择"
                  style={{
                    width: '100%',
                  }}
                >
                  <Option value="0">x_ef</Option>
                  <Option value="1">x_1b</Option>
                </Select>,
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <FormItem label="指标名称">
              {getFieldDecorator('microservice_name')(
                <Select
                  placeholder="请选择"
                  style={{
                    width: '100%',
                  }}
                >
                  <Option value="0">MSIC</Option>
                  <Option value="1">MSMC</Option>
                </Select>,
              )}
            </FormItem>
          </Col>
          <Col md={8} sm={24}>
            <span className={styles.submitButtons}>
              <Button type="primary" htmlType="submit">
                查询
              </Button>
              <Button
                style={{
                  marginLeft: 50,
                }}
              >
                导出数据
              </Button>
            </span>
          </Col>
        </Row>
      </Form>
    );
  }

  render() {
    const columns = [
      {
        title: '指标名称',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: '值',
        dataIndex: 'value',
        key: 'value',
      },
    ];

    return (
      <PageHeaderWrapper>
        <div className={styles.tableListForm}>{this.renderForm()}</div>
        <Row gutter={24}>
          <Col xl={16} lg={24} md={24} sm={24} xs={24}>
            <Card
              style={{
                marginBottom: 24,
              }}
              title="可维护性指标折线图"
              bordered={false}
              bodyStyle={{
                padding: 0,
              }}
            >
              <div id="maintain-chart" style={{ width: '100%', height: 650 }}>
                {' '}
              </div>
            </Card>
          </Col>
          <Col xl={8} lg={24} md={24} sm={24} xs={24}>
            <Card
              style={{
                marginBottom: 24,
              }}
              title="指标展示"
              bordered={false}
              bodyStyle={{
                padding: 0,
              }}
            >
              <Table dataSource={versionNames} columns={columns} />;
            </Card>
          </Col>
        </Row>
      </PageHeaderWrapper>
    );
  }
}

// export default Maintain;
export default Form.create()(Metrics);
