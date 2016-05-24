using cn.bmob.io;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MarkIt.SignInAndSignUp.Model
{
    class TestContactObject:BmobTable
    {
        //对应的数据表 联系人对应的数据表名为：“Contact”
        private String fTable;

        public string contactName { get; set; }
        public BmobPointer<BmobUser> user { get; set; }

        //构造函数
        public TestContactObject() { }

        //构造函数
        public TestContactObject(String tableName)
        {
            this.fTable = tableName;
        }

        public override string table
        {
            get
            {
                if (fTable != null)
                {
                    return fTable;
                }
                return base.table;
            }
        }

        public override void readFields(BmobInput input)
        {
            base.readFields(input);
            //读取属性值
            this.contactName = input.getString("contactName");
            this.user = input.Get<BmobPointer<BmobUser>>("user");
        }

        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);
            //写到发送端
            output.Put("contactName", this.contactName);
            output.Put("user", this.user);
        }
    }
}
