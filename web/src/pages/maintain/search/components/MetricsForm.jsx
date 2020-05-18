import { Form, Row, Col, Select, Input, Button, Card, Icon, List, Typography, Table, Tabs } from 'antd';
const { TabPane } = Tabs;
import React from 'react';
import styles from "@/pages/maintain/search/style.less";

const FormItem = Form.Item;

const MetricsForm = props => {
  const { form, searchBack, metrics } = props;

  const okHandle = (e) => {
    e.preventDefault();
    form.validateFields((err, fieldsValue) => {
      if (err) return;
      // form.resetFields();
      searchBack(fieldsValue);
    });
  };

  return (
    <Form onSubmit={okHandle} layout="inline">
      <Row
        gutter={{
          md: 8,
          lg: 24,
          xl: 48,
        }}
      >
        <Col md={8} sm={24}>
          <FormItem label="微服务名称">
            {form.getFieldDecorator('microservice_name',{
              rules: [{ required: false, message: 'Please select your microservice!' }],
            })(
              <Select
                style={{ width: 120 }}
              >
                {metrics.microservices.map(m => (
                  <Option key={m}>{m}</Option>
                ))}
              </Select>
            )}
          </FormItem>
        </Col>
        <Col md={8} sm={24}>
          <FormItem label="指标名称">
            {form.getFieldDecorator('metric_name',
              {rules: [{ required: true, message: 'Please select your metrics!' }],})(
              <Select
                style={{ width: 120 }}
              >
                {metrics.metrics.map(m => (
                  <Option key={m}>{m}</Option>
                ))}
              </Select>
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
};

export default Form.create()(MetricsForm);
