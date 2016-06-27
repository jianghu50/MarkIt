using cn.bmob.io;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MarkIt.MainInterface.Model
{
    class TestNoteObject: BmobTable
    {
        //对应的数据表 笔记对应的数据表名为：“Note”
        private String fTable;

        public string text {
            get; set;
        }

        public BmobFile image {
            get; set;
        }

        public BmobPointer<TestContactObject> contact {
            get; set;
        }

        //构造函数
        public TestNoteObject()
        {
        }

        //构造函数
        public TestNoteObject(String tableName)
        {
            this.fTable = tableName;
        }

        public override string table {
            get {
                if(fTable != null) {
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
            this.contact = input.Get<BmobPointer<TestContactObject>>("contact");
        }

        public override void write(BmobOutput output, Boolean all)
        {
            base.write(output, all);

            output.Put("text", this.text);
            output.Put("image", this.image);
            output.Put("contact", this.contact);
        }

    }
}
