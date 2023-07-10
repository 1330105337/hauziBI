import {useState} from "react";
import {listUserVOByPageUsingPOST} from "@/services/yubi/userController";
import {message} from "antd";


const initSearchParams={
  pageSize:12,
};
const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({...initSearchParams});
const [userList,setUserList]=useState<API.User[]>(()=>[]);
const [total,setTotal]=useState<number>(0);

const dataSource=async ()=>{
  const res = await listUserVOByPageUsingPOST(searchParams);
  try {
    if (res.data) {
      setUserList(res.data.records ?? []);
      setTotal(res.data.total ?? 0);

    } else {
      message.error('获取我的图表失败');
    }
  } catch (e:any) {
    message.error('获取我的图表失败,'+e.message);
  }
}
