import { createRouter, createWebHistory } from 'vue-router'
import { authGuard, passwordVerificationGuard, ownerGuard } from './guards'

import DefaultLayout from '@/layouts/DefaultLayout.vue'
import AuthLayout from '@/layouts/AuthLayout.vue'
import HomeView from '@/views/home/HomeView.vue'
import LogInView from '@/views/member/LogInView.vue'
import SignUpView from '@/views/member/SignUpView.vue'
import MainView from '@/views/main/MainView.vue'
import memoView from '@/views/memo/MemoView.vue'
import ScheduleView from '@/views/Schedule/ScheduleView.vue'
import NoticeDetail from '@/views/notice/NoticeDetail.vue'
import VideoInterviewView from '@/views/videoInterview/VideoInterviewView.vue'
import SalaryView from '@/views/Salary/SalaryView.vue'
import MyPageView from '@/views/member/MyPage.vue'
import PasswordCheckView from '@/views/member/PasswordCheckView.vue'
import VideoInterviewResultView from '@/views/videoInterview/VideoInterviewResultView.vue'
import NotFoundView from '@/views/error/NotFoundView.vue'
import ForbiddenView from '@/views/error/FobbidenView.vue'
import InterviewLayout from '@/layouts/InterviewLayout.vue'
import FooterPageLayout from '@/layouts/FooterPageLayout.vue'
import CustomerSupport from '@/views/info/CustomerSupport.vue'
import CompanyInfo from '@/views/info/CompanyInfo.vue'

import ReviewPage from '@/views/review/ReviewPage.vue';
import AddComponents from '@/views/addComponents/AddComponents.vue'
import { decodeToken } from '@/common/apis/utils/tokenUtils';

const router = createRouter({
  history: createWebHistory("/"),
  routes: [
    {
      // 홈페이지 (DefaultLayout 없이 렌더링)
      path: '/',
      name: 'home',
      component: HomeView,
      beforeEnter: (to, from, next) => {
        const isAuthenticated = localStorage.getItem('access_token') !== null;

        if (isAuthenticated) {
          const accessToken = localStorage.getItem('access_token');
          const decodedToken = decodeToken(accessToken);
          const currentTime = Date.now() / 1000;
          if (decodedToken.exp > currentTime) {
            next({ name: 'main' });
          } else {
            next();
          }
        } else {
          next();
        }

      }

    },
    {
      path: '/',
      component: DefaultLayout,
      children: [
        {
          // 메인 페이지
          path: 'main',
          name: 'main',
          component: MainView,
          meta: { requiresAuth: true }
        },
        {
          // 마이 페이지
          path: 'myPage',
          name: 'myPage',
          component: MyPageView,
          meta: { requiresAuth: true, requiresPasswordVerification: true }
        },
        {
          // 알림장 페이지
          path: 'memo',
          name: "memo",
          component: memoView,
          meta: { requiresAuth: true }
        },
        {
          // 스케줄 페이지
          path: 'schedule',
          name: 'schedule',
          component: ScheduleView,
          meta: { requiresAuth: true }
        },
        {
          // 공지사항 세부 페이지
          path: 'notice',
          name: 'NoticeDetail',
          component: NoticeDetail,
          meta: { requiresAuth: true }
        },
        {
          // 스케줄 페이지
          path: '/schedules',
          name: 'schedules',
          component: ScheduleView,
          meta: { requiresAuth: true }
        },
        {
          // 급여 페이지
          path: '/salary',
          name: 'salary',
          component: SalaryView,
          meta: { requiresAuth: true }
        },
        {
          // 고객 리뷰 페이지
          path: '/review',
          name: 'review',
          component: ReviewPage
        },
        {
          path: '/add-components',
          name: 'add-components',
          component: AddComponents
        }
      ]
    },
    {
      path: '/',
      component: AuthLayout,
      children: [
        {
          path: 'login',
          name: 'login',
          component: LogInView
        },
        {
          path: 'signup',
          name: 'signup',
          component: SignUpView
        },
        {
          path: 'passwordCheck',
          name: 'passwordCheck',
          component: PasswordCheckView,
          meta: { requiresAuth: true }
        }
      ]
    },
    {
      // 비디오 인터뷰 관련 페이지
      path: '/',
      component: InterviewLayout,
      children: [
        {
          path: 'video-interview',
          name: 'video-interview',
          component: VideoInterviewView,
          children: [
            {
              path: ':id',
              name: 'video-interview-worker',
              component: VideoInterviewView
            }
          ]
        },
        {
          path: 'interview-result',
          name: 'interview-result',
          component: VideoInterviewResultView,
          meta: { requiresAuth: true, requiresOwner: true }
        }
      ]
    },
    {
      path: '/info',
      component: FooterPageLayout,
      children: [
        {
          path: 'company',
          name: 'CompanyInfo',
          component: CompanyInfo
        },
        {
          path: 'support',
          name: 'CustomerSupport',
          component: CustomerSupport
        }
      ]
    },
    {
      // 에러 페이지
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: NotFoundView
    },
    {
      path: '/forbidden',
      name: 'forbidden',
      component: ForbiddenView
    }
  ]
})

// 가드 분리했음음
router.beforeEach(authGuard);
router.beforeEach(passwordVerificationGuard);
router.beforeEach(ownerGuard);
// // 인증이 필요한 라우트 보호
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = localStorage.getItem('access_token') !== null;

//   // 인증이 필요한 페이지 목록
//   const authRequiredPages = ['main', 'memo', 'schedule'];

//   if (authRequiredPages.includes(to.name) && !isAuthenticated) {
//     // 인증이 필요한 페이지인데 로그인이 안 된 경우
//     alert('로그인이 필요한 서비스입니다.');
//     next('/login');
//   } else if (to.name === 'login' && isAuthenticated) {
//     // 이미 로그인된 상태에서 로그인 페이지 접근 시
//     next('/main');
//   } else {
//     next();
//   }
// });

export default router

