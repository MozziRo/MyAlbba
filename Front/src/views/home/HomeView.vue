<template>
  <div class="home-view" ref="homeView">
    <section
      v-for="(slide, index) in slides"
      :key="index"
      class="onboarding-slide"
      ref="slideRefs"
    >
      <div v-if="index === 0 || index === 3" class="content-center fadeInUp">
        <h1 class="slide-title fadeInUp">{{ slide.title }}</h1>
        <p class="slide-description fadeInUp">{{ slide.description }}</p>
      </div>

      <div v-if="index === 1" class="content content-left fadeInUp">
        <Calendar class="calendar fadeInLeft" />
        <div class="text-container">
          <h1 style="width: max-content;" class="slide-title fadeInUp">{{ slide.title }}</h1>
          <p class="slide-description fadeInRight">{{ slide.description }}</p>
        </div>
      </div>

      <div v-if="index === 2" class="content content-right fadeInUp">
        <Salary class="salary-container" />
        <div class="text-container">
          <h1 style="width: max-content;" class="slide-title fadeInUp">{{ slide.title }}</h1>
          <p class="slide-description fadeInLeft">{{ slide.description }}</p>
        </div>
      </div>

      <div v-if="index === 3" class="content content-center fadeInUp">
        <AlarmPage class="alarm-container" />
        <div class="text-container"></div>
      </div>


      <!-- 화상 면접 기능 -->
      <div v-if="index === 4" style="width: 1000px;" class="content content-right fadeInUp video">
        <div class="text-container">
          <h1 class="slide-title fadeInRight">{{ slide.title }}</h1>
          <p class="slide-description fadeInUp">{{ slide.description }}</p>
        </div>
        <VideoInterview style="margin-right: 0;" class="video-container"/>
      </div>

      <button v-if="index === slides.length - 1" @click="startApp" class="start-button fadeInUp">
        시작하기
      </button>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import Calendar from "./Calendar.vue";
import AlarmPage from "./AlarmPage.vue";
import Salary from "./Salary.vue";
import VideoInterview from "./Video-Interview.vue";

const router = useRouter();
const slideRefs = ref([]);
const homeView = ref(null);
const currentIndex = ref(0);

const slides = ref([
  { title: "내알빠다에 오신 것을 환영합니다!", description: "근무 관리의 새로운 기준" },
  { title: "일정 관리가 쉬워집니다!", description: "간단한 드래그 & 드롭으로 일정을 추가하세요." },
  { title: "급여도 한눈에 확인하세요!", description: "미지급 급여도 바로 확인할 수 있어요." },
  { title: "근무 인수인계는 확실하게!", description: "전하고 싶은 인계사항을 특정 근무자에게 보낼 수 있어요." },
  { title: "화상 면접 후 인공지능과 함께 분석해요!", description: "면접 중 메모를 기록하고 AI 분석을 통해 지원자의 적합성을 확인할 수 있어요." }
]);

const startApp = () => {
  router.push("/main");
};

onMounted(() => {
  nextTick(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            currentIndex.value = slideRefs.value.indexOf(entry.target);
            entry.target.classList.add("visible");
          }
        });
      },
      { threshold: 0.5 }
    );

    slideRefs.value.forEach((slide) => {
      observer.observe(slide);
    });
  });
});
</script>


<style scoped>
/*  배경 스타일 */
.home-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100vh;
  overflow-y: scroll;
  scroll-snap-type: y mandatory;
  background: linear-gradient(to bottom, #2D3250,  #424769);
  color: #fff;
}

/*  슬라이드 */
.onboarding-slide {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  flex-direction: column;
  padding: 5rem 1rem;
  scroll-snap-align: start;
  transition: opacity 1s ease-out, transform 1s ease-out;
  opacity: 0;
  transform: scale(0.9);
  padding-top: 3rem;
  padding-bottom: 3rem;
}

.onboarding-slide.visible {
  opacity: 1;
  transform: scale(1);
}

/*  콘텐츠 정렬 */
.content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1000px;
  width: 100%;
  text-align: center;
}

.content-center {
  flex-direction: column;
  text-align: center;
  justify-content: center;
}

.content-left {
  flex-direction: row-reverse;
  text-align: right;
}

.content-right {
  flex-direction: row;
  text-align: left;
}

/*  텍스트 컨테이너 */
.text-container {
  max-width: 450px;
  width: 100%;
}

/*  일정 캘린더 */
.calendar {
  width: 50%;
  max-width: 450px;
  min-height: 300px;
  margin-top: 2rem;
  overflow: hidden;
}

/*  급여 계산 UI */
.salary-container {
  width: 50%;
  max-width: 450px;
  min-height: 300px;
  margin-top: 2rem;
  margin-bottom: 2rem;
  overflow: hidden;
}

/*  인수인계 UI */
.alarm-container {
  width: 50%;
  max-width: 450px;
  min-height: 300px;
  margin-top: 2rem;
  margin-bottom: 2rem;
  overflow: hidden;
}

/* 화상 면접 */
.video-container {
  width: 50%;
  max-width: 450px;
  min-height: 300px;
  margin-top: 2rem;
  margin-bottom: 2rem;
}

.video {
  width: 200% !important;
}


/*  폰트 스타일 */
.slide-title {
  font-size: 3rem;
  font-weight: 700;
  color: #fff;
  width: auto
}

.slide-description {
  font-size: 1.3rem;
  color: #f7f4f4;
}

/*  애니메이션 */
.fadeInUp {
  opacity: 0;
  transform: translateY(50px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}

.fadeInLeft {
  opacity: 0;
  transform: translateX(-100px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}

.fadeInRight {
  opacity: 0;
  transform: translateX(100px);
  transition: opacity 0.8s ease-out, transform 0.8s ease-out;
}

/*  슬라이드가 화면에 보일 때 활성화 */
.visible .fadeInUp,
.visible .fadeInLeft,
.visible .fadeInRight {
  opacity: 1;
  transform: translateX(0) translateY(0);
}

/*  시작 버튼 */
.start-button {
  padding: 1rem 2rem;
  font-size: 1.4rem;
  font-weight: bold;
  background-color: #F9B17A;
  color: #2D3250;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 2.5rem;
  align-self: center;
}

.start-button:hover {
  background-color:  #FCDDBD;
  transform: scale(1.05);
}
</style>

