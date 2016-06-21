using cn.bmob.io;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MarkIt.SignInAndSignUp.Model
{
    class NoteObject:BmobTable
    {
        //对应的数据表 笔记对应的数据表名为：“Note”
        private String fTable;

        public string text { get; set; }

        public BmobFile image { get; set; }

        public BmobPointer<ContactObject> contact { get; set; }

        //软删除，删除时标记为true
        public BmobBoolean isDelete { get; set; }

        //构造函数
        public NoteObject() { }

        //构造函数
        public NoteObject(String tableName)
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

            this.text = input.getString("text");
            this.image = input.Get<BmobFile>("image");
            this.contact = input.Get<BmobPointer<ContactObject>>("contact");
            this.isDelete = input.getBoolean("isDelete");
        }

        public override void write(BmobOutput output, Boolean all)
        {
            base.write(output, all);

            output.Put("text", this.text);
            output.Put("image", this.image);
            output.Put("contact", this.contact);
            output.Put("isDelete", this.isDelete);
        }

    }
}
