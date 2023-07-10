import {Table, Input, message} from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import React, { useEffect, useState } from 'react';
import {listUserByPageUsingPOST, listUserVOByPageUsingPOST} from "@/services/yubi/userController";

const BasicInfoTable = () => {
    const [userList, setUserList] = useState<API.User[]>();
    console.log(userList);
  const initSearchParams = {
    current: 1,
    pageSize: 4,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  const [searchParams, setSearchParams] = useState<API.UserQueryRequest>({ ...initSearchParams });
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);



  useEffect(() => {async function fetchData() {
    try {
      const res = await listUserVOByPageUsingPOST(searchParams);
      if (res.data) {
        setUserList(res.data.records ?? []);
        setTotal(res.data.total ?? 0);
      } else {
        message.error('获取我的图表失败');
      }

    } catch (e: any) {
      message.error('获取我的图表失败，' + e.message);
    }
    setLoading(false);
  };
    setUserList(userList);
    fetchData();
  }, []);

  const columns = [
    {
      title: '编号',
      dataIndex: 'id',
      key: 'id'
    },
    {
      title: '用户名',
      dataIndex: 'userName',
      key: 'name'
    },
    {
      title: '性别',
      dataIndex: 'userRole',
      key: 'userRole'
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime'
    },

  ];
  return (
    <>
      <Input.Search
        placeholder="全局搜索"
        enterButton={<SearchOutlined />}
        onSearch={(value) => {
          // 设置搜索条件
          setSearchParams({
            ...initSearchParams,
            userName:value,
          })}}
      />
      <br />
      <Table
        dataSource={userList}
        columns={columns}
        pagination={{
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            })
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: total,
        }}
      />
    </>
  );
};

export default BasicInfoTable;


