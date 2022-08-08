import Vue from "vue";
import Router from "vue-router";
//import HelloWorld from "@/components/HelloWorld";
import Login from "@/components/Login";
import Home from "@/components/Home";
import Course from "@/components/Course";
import Courses from "@/components/Courses";
import CoursesAdmin from "@/components/CoursesAdmin";
import Admin from "@/components/Admin";
import ClassList from "@/components/ClassList";
import ClassDetail from "@/components/ClassDetail";
import UserDetail from "@/components/UserDetail";
import UserSidebar from "@/components/side/UserSidebar";
import CoursesSidebar from "@/components/side/CoursesSidebar";
import AdminSidebar from "@/components/side/AdminSidebar";
import NoticeList from "@/components/NoticeList";
import NoticeDetail from "@/components/NoticeDetail"
import ResourceList from "@/components/ResourceList";
import PostList from '@/components/PostList';
import PostDetail from '@/components/PostDetail';
import TeacherAssignmentList from '@/components/assignment/TeacherAssignmentList'
import TeacherAssignmentDetail from '@/components/assignment/TeacherAssignmentDetail'
import StudentAssignmentList from '@/components/assignment/StudentAssignmentList'
import StudentAssignmentDetail from '@/components/assignment/StudentAssignmentDetail'

Vue.use(Router);
const router=new Router({
  mode: "history",
  routes: [
    /*
    {
      path: "/",
      name: "HelloWorld",
      component: HelloWorld,
    },

     */
    {
      path: "/home",
      name: "Home",
      component: Home,
    },
    {
      path: "/course",
      name: "Course",
      component: Course,
    },
    {
      path: "/coursesAdmin",
      name: "CoursesAdmin",
      component: CoursesAdmin,
    },
    {
      path: "/courses",
      name: "Courses",
      component: Courses,
    },
    {
      path: "/admin",
      name: "Admin",
      component: Admin,
    },
    {
      path: "/home/userSidebar",
      name: "UserSidebar",
      component: UserSidebar,
    },
    {
      path: "/courses/coursesSidebar",
      name: "CoursesSidebar",
      component: CoursesSidebar,
    },
    {
      path: "/admin/adminSidebar",
      name: "AdminSidebar",
      component: AdminSidebar,
    },


    {
      path: "/login",
      name: "Login",
      component: Login,
    },
    {
      path: "/classes",
      name: "ClassList",
      component: ClassList,
    },
    {
      path: "/class/:id",
      name: "ClassDetail",
      component: ClassDetail,
    },
    {
      path: "/profile/:id",
      name: "UserDetail",
      component: UserDetail,
    },
    {
      path: "/notices/:sectionID",
      name: "NoticeList",
      component: NoticeList,
    },
    {
      path: "/notice/:id",
      name: "NoticeDetail",
      component: NoticeDetail,
    },
    {
      path: "/resources/:sectionID",
      name: "ResourceList",
      component: ResourceList,
    },
    {
      path: "/posts",
      name: "PostList",
      component: PostList,
    },
    {
      path: "/post/:id",
      name: "PostDetail",
      component: PostDetail,
    },
    {
      path: "/teacher_assignments/:sectionID",
      name: "TeacherAssignmentList",
      component: TeacherAssignmentList,
    },
    {
      path: "/teacher_assignment/:sectionID/:assignmentID",
      name: "TeacherAssignmentDetail",
      component: TeacherAssignmentDetail,
    },
    {
      path: "/student_assignments/:sectionID",
      name: "StudentAssignmentList",
      component: StudentAssignmentList,
    },
    {
      path: "/student_assignment/:sectionID/:assignmentListID",
      name: "StudentAssignmentDetail",
      component: StudentAssignmentDetail,
    },
  ],
});

router.beforeEach(function (to, from ,next) {
  if (to.path === '/login') {
    next();
  } else {
    if (localStorage.getItem("token")) {

      console.log("token存在"+to.path);
      var path=to.path;
      var paths=path.split('/');
      console.log("path"+paths[1]+"  "+paths);
      var userDetails=JSON.parse(localStorage.getItem("userDetails"));

      if(userDetails.authorities[0].authority === "Role_Teacher"){
        if(paths[1]==="admin"||paths[1]==="coursesAdmin"||paths[1]==="student_assignments"||paths[1]==="student_assignment"){
          next('/home')
        }
      }else if(userDetails.authorities[0].authority === "Role_Student"){
        if(paths[1]==="admin"||paths[1]==="coursesAdmin"||paths[1]==="teacher_assignments"||paths[1]==="teacher_assignment"){
          next('/home')
        }
      }else if(userDetails.authorities[0].authority === "Role_Parent"){
        if(paths[1]==="admin"||paths[1]==="coursesAdmin"||paths[1]==="teacher_assignments"||paths[1]==="teacher_assignment"){
          next('/home')
        }
      }
      else if(userDetails.authorities[0].authority === "Role_Admin"){
        if(paths[1] === "profile"||paths[1] === "home" || paths[1] === "course"||paths[1]==="student_assignments"||path[1]==="teacher_assignment"){
          next('/admin')
        }
      }
      next()
    } else {
      console.log("token不存在");
      next('/login')
    }
  }


});

export default router;
