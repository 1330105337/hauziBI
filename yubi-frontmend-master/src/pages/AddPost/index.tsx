import { genChartByAiUsingPOST } from '@/services/yubi/chartController';
import { UploadOutlined } from '@ant-design/icons';
import {Avatar, Button, Card, Col, Divider, Form, Input, List, message, Row, Select, Space, Spin, Upload} from 'antd';
import TextArea from 'antd/es/input/TextArea';
import React, { useState } from 'react';
import ReactECharts from 'echarts-for-react';
import {genPostByAiUsingPOST} from "@/services/yubi/postController";

/**
 * 添加图表页面
 * @constructor
 */
const AddPost: React.FC = () => {
  const [post, setPost] = useState<API.PostResponse>();
  const [option, setOption] = useState<any>();
  const [submitting, setSubmitting] = useState<boolean>(false);

  /**
   * 提交
   * @param values
   */
  const onFinish = async (values: any) => {
    // 避免重复提交
    if (submitting) {
      return;
    }
    setSubmitting(true);
    setPost(undefined);
    setOption(undefined);
    // 对接后端，上传数据
    const params = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genPostByAiUsingPOST(params, {});
      if (!res?.data) {
        message.error('回答失败');
      } else {
        console.log(res.data.genResult);
        setPost(res.data);
        message.success('回答成功，请查看左方');
      }
    } catch (e: any) {
      message.error('分析失败，' + e.message);
    }
    setSubmitting(false);
  };

  return (
    <div className="add-post">

            <Form name="addPost" labelAlign="left" labelCol={{ span: 4 }}
                  wrapperCol={{ span: 16 }} onFinish={onFinish} initialValues={{}}>
              <Form.Item
                name="name"
                label="问题类型"
                rules={[{ required: true, message: '请输入问题类型' }]}
              >
                <TextArea placeholder="请输入你的问题类型，比如：java后端，html，python" />
              </Form.Item>
              <Form.Item name="question" label="问题概述">
                <Input placeholder="请输入问题" />
              </Form.Item>

              <Form.Item wrapperCol={{ span: 16, offset: 4 }}>
                <Space>
                  <Button type="primary" htmlType="submit" loading={submitting} disabled={submitting}>
                    提交
                  </Button>
                  <Button htmlType="reset">重置</Button>
                </Space>
              </Form.Item>
            </Form>
        <Col span={25}>
          <Card title="问题回答" >
            {post?.genResult ?? <div>请先在上方进行提交</div>}
            <Spin spinning={submitting}/>
          </Card>
          <Divider />
        {/*  <Card title="可视化图表">*/}
        {/*    {*/}
        {/*      option ? <ReactECharts option={option} /> : <div>请先在左侧进行提交</div>*/}
        {/*    }*/}
        {/*    <Spin spinning={submitting}/>*/}
        {/*  </Card>*/}
        </Col>

    </div>
  );
};
export default AddPost;
