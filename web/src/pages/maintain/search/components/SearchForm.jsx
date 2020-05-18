import { Form, Row, Col, Select, Input, Button, Card, Icon, List, Typography, Table, Tabs } from 'antd';
const { TabPane } = Tabs;
import React from 'react';
import styles from "@/pages/maintain/search/style.less";

const FormItem = Form.Item;

const SearchForm = props => {
  const { form, searchBack, selectVersion, search } = props;

  const okHandle = (e) => {
    e.preventDefault();
    form.validateFields((err, fieldsValue) => {
      if (err){return}
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
          <FormItem label="版本号">
            {form.getFieldDecorator('version_name',{
              rules: [{ required: false, message: 'Please select your version!' }],
            })(
              <Select
                style={{ width: 120 }}
                onChange={selectVersion}
              >
                {search.versions.map(version => (
                  <Option key={version}>{version}</Option>
                ))}
              </Select>
            )}
          </FormItem>
        </Col>
        <Col md={8} sm={24}>
          <FormItem label="微服务名称">
            {form.getFieldDecorator('microservice_name',
              {rules: [{ required: true, message: 'Please select your microservice!' }],})(
              <Select
                style={{ width: 120 }}
              >
                {search.microservices.map(m => (
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

export default Form.create()(SearchForm);
