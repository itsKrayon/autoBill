<template>
  <el-container>
    <el-header >
      <el-page-header @back="goBack" style="float:left;width: 400px" ></el-page-header>
      <UserSidebar v-bind:menuIndex="menuIndex" style="width: 500px;float: right"></UserSidebar>
    </el-header>
    <el-main style="">
      <div v-show="show">
      <div style="" >
        <el-table
        style="width: 100%"
        :data="getFiveNotiecs">
          <el-table-column
            label="标题"
            prop="title"
          ></el-table-column>
          <el-table-column
            label="公告简介"
            prop="description"
            ></el-table-column>
          <el-table-column
            label="创建时间"
            prop="createTime"
            ></el-table-column>
          <el-table-column
            label="操作"
            >
            <template slot-scope="scope">
              <el-button-group>
                <el-button @click="getNotice(scope.row.id)" plain>详情</el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div >

        <el-table
          style="width: 100%"
          title="作业"
          :data="assignment"
        >

          <el-table-column
            label="作业"
            prop="title"
            width=""></el-table-column>
          <el-table-column
            label="简介"
            prop="description"
            width=""></el-table-column>
          <el-table-column
            label="状态"
            prop="status"
            width=""></el-table-column>
          <el-table-column
            label="提交日期"
            prop="submitDatetime"
            width=""></el-table-column>
          <el-table-column
            label="截止日期"
            prop="deadline"
            width=""></el-table-column>
          <el-table-column
            label="操作"
            width="">
            <template slot-scope="scope">
              <el-button-group>
                <el-button @click="getAssignment(scope.row.id)" plain>详情</el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
      </div>
      </div>
      <div v-show="hello">
        <div class="inner">
          <div class="backCSS">
          </div>
        </div>
      </div>
      <el-empty v-show="empty" description="暂无课程"></el-empty>
    </el-main>
  </el-container>

</template>

<script>
  import UserSidebar from './side/UserSidebar'
  export default {
    name: "Home",
    components:{
      UserSidebar
    },
    data () {
      return {
        notices: [{
          id: 4,
          title: "",
          description: "",
          content: "",
          creatorId: 1,
          createTime: "",
          classId: 1,
          sectionId: 3
        }],
        assignment:[{
          id:'',
          title:'',
          description:'',
          status:'',
          submitDatetime:'',
          isExpired:'',
          deadline:'',
          comment:''
        }],

        identity:'',
        show:false,
        menuIndex:1,
        loading: false,
        empty:false,
        hello:false
      }
    },
    computed: {
      getFiveNotiecs: function () {
        return this.notices.length >= 5 ? this.notices.slice(0,5) : this.notices;
      }
    },
    created() {
      const loading = this.$loading({
        lock: true,
      });
      localStorage.setItem('menuIndex',1);
      var userDetails=JSON.parse(localStorage.getItem('userDetails'));
      console.log(userDetails);
      if (userDetails.role===1){
        this.show=false;
      }else if (userDetails.role===2||userDetails.role===3) {
        this.show = true;
      }
        this.$axios
          .get("/notice/list", {
            params:{
              classId: "1"
            }
          })
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.notices = res.data;

          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取公告失败");
          });
        this.$axios
          .get("/assignment/toDoList")
          .then((res) => {
            loading.close();
            console.log(res.data);
            this.assignment = res.data;

          })
          .catch((error) => {
            loading.close();
            console.log(error);
            this.$message.error("获取作业失败");
          });

        if (this.show===false){
          this.hello=true;
          this.empty=false;
        } else if((this.notices.length ===0 && this.assignment.length === 0)){
          this.empty=true;
          this.hello=false;
        }else{
          this.hello=false;
          this.empty=false
        }
        console.log("empty:"+this.empty+this.assignment.length+"  "+this.notices.length)
    },
    methods:{
      getNotice(id){
        let path="/notices/";
        path+=id;
        console.log("path: "+path);
        this.$router.push({path: path})
      },
      getAssignment(id){
        let path="";
        var userDetails=JSON.parse(localStorage.getItem("userDetails"));
        console.log("nnnpath: "+path+"  "+userDetails);
        console.log("nnnpath: "+path+"  "+userDetails.authorities);
        if(userDetails.authorities[0].authority === "Role_Teacher"){
          path="/teacher_assignments/"+id;
        }else if(userDetails.authorities[0].authority === "Role_Student"){
          path="/student_assignments/"+id;
        }
        console.log("path: "+path);
        this.$router.push({path: path})
      },
      goBack() {
        this.$router.go(-1);
      },
    }
  }
</script>

<style scoped>
  .backCSS{
    background: url("../assets/background.jpeg") repeat;
    background-position: center;
    background-position-x: center;
    background-position-y: center;
    border-radius: 50%;
    height: 400px;
    width: 400px;
    background-size: cover;

    position: relative;
    float: left;
    left: -50%;
  }
  .inner{
    position: absolute;
    left: 50%;
  }
</style>
