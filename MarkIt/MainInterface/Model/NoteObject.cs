using System;
using cn.bmob.io;

namespace MarkIt.MainInterface
{
    class NoteObject: BmobTable
    {
        //对应的数据表 笔记对应的数据表名为：“Note”
        private const String tableName = "Note";

        public string text {
            get; set;
        }

        public BmobFile image {
            get; set;
        }

        public BmobPointer<ContactObject> contact {
            get; set;
        }

        public BmobBoolean isDelete {
            get; set;
        }

        //构造函数
        public NoteObject()
        {
        }

        public override string table {
            get {
                return tableName;
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
