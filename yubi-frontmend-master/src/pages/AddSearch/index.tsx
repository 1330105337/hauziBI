// import {Tabs, Input, message, List, Card, Avatar, Result} from 'antd';
// import React, {useEffect, useState} from 'react'
// import Search from "antd/es/input/Search";
// import {searchAllUsingPOST} from "@/services/yubi/searchController";
// const { TabPane } = Tabs;
// function App() {
//   const initSearchParams = {
//     current: 1,
//     pageSize: 4,
//     sortField: 'createTime',
//     sortOrder: 'desc',
//   };
//
//   const [searchParams, setSearchParams] = useState<API.SearchRequest>({ ...initSearchParams });
//   const [searchText, setSearchText] = useState('');
//   const [activeKey, setActiveKey] = useState('post');
//   const [postList, setPostList] = useState<API.MessageVO[]>();
//   const [pictureList, setPictureList] = useState<API.Picture[]>();
//   const [userList, setUserList] = useState<API.User[]>();
//   const [loading, setLoading] = useState<boolean>(true);
//
//   const loadData = async () => {
//     setLoading(true);
//     try {
//       const res = await searchAllUsingPOST(searchParams);
//       if (res.data) {
//         setUserList(res.data.userList);
//         setPostList(res.data.messageList);
//         setPictureList(res.data.messageList);
//       } else {
//         message.error('获取我的图表失败');
//       }
//     } catch (e: any) {
//       message.error('获取我的图表失败，' + e.message);
//     }
//     setLoading(false);
//   };
//
//   useEffect(() => {
//     loadData();
//   }, [searchParams]);
//   return (
//     <div className="index-page">
//       <div>
//         <Search placeholder="请输入问题名称" enterButton loading={loading} onSearch={(value) => {
//           // 设置搜索条件
//           setSearchParams({
//             ...initSearchParams,
//             searchText: value,
//           })
//         }}/>
//       </div>
//
//       <List
//         grid={{
//           gutter: 16,
//           xs: 1,
//           sm: 1,
//           md: 1,
//           lg: 2,
//           xl: 2,
//           xxl: 2,
//         }}
//         pagination={{
//           onChange: (page, pageSize) => {
//             setSearchParams({
//               ...searchParams,
//               current: page,
//               pageSize,
//             })
//           },
//           current: searchParams.current,
//           pageSize: searchParams.pageSize,
//         }}
//         loading={loading}
//         dataSource={postList}
//         renderItem={(item) => (
//             <Card style={{ width: '100%' }}>
//           title={item.content}
//             </Card>
//         )}
//       />
//       {/*<Tabs activeKey={activeKey} onChange={onTabChange}>*/}
//       {/*  <TabPane key="post" tab="文章">*/}
//       {/*    /!*<PostList postList={postList} />*!/*/}
//       {/*    {JSON.stringify(postList)}*/}
//       {/*  </TabPane>*/}
//       {/*  <TabPane key="picture" tab="图片">*/}
//       {/*   */}
//
//       {/*  </TabPane>*/}
//       {/*  <TabPane key="user" tab="用户">*/}
//       {/*    /!*<UserList userList={userList} />*!/*/}
//       {/*  </TabPane>*/}
//       {/*</Tabs>*/}
//     </div>
//   );
// }
//
// export default App;
//
//
//
//
