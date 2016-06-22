using System;
using cn.bmob.io;

namespace MarkIt.MainInterface
{
    class ContactObject: BmobTable
    {
        private const String tableName = "Contact";

        public String contactName {
            get; set;
        }

        public BmobPointer<BmobUser> user {
            get; set;
        }

        public BmobBoolean isDelete {
            get; set;
        }

        public ContactObject()
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

            this.contactName = input.getString("contactName");
            this.user = input.Get<BmobPointer<BmobUser>>("user");
            this.isDelete = input.getBoolean("isDelete");
        }

        public override void write(BmobOutput output, bool all)
        {
            base.write(output, all);

            output.Put("contactName", this.contactName);
            output.Put("user", this.user);
            output.Put("isDelete", this.isDelete);
        }
    }
}
