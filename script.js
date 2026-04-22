const slides = Array.from(document.querySelectorAll('.slide'));
const dotsContainer = document.getElementById('dots');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');

let current = 0;
let timer;

function buildDots() {
  slides.forEach((_, index) => {
    const dot = document.createElement('button');
    dot.className = 'dot';
    dot.setAttribute('aria-label', `跳转到第 ${index + 1} 张`);
    dot.addEventListener('click', () => {
      current = index;
      render();
      restartAutoPlay();
    });
    dotsContainer.appendChild(dot);
  });
}

function render() {
  slides.forEach((slide, idx) => {
    slide.classList.toggle('active', idx === current);
  });

  const dots = dotsContainer.querySelectorAll('.dot');
  dots.forEach((dot, idx) => {
    dot.classList.toggle('active', idx === current);
  });
}

function next() {
  current = (current + 1) % slides.length;
  render();
}

function prev() {
  current = (current - 1 + slides.length) % slides.length;
  render();
}

function restartAutoPlay() {
  clearInterval(timer);
  timer = setInterval(next, 4500);
}

prevBtn.addEventListener('click', () => {
  prev();
  restartAutoPlay();
});

nextBtn.addEventListener('click', () => {
  next();
  restartAutoPlay();
});

buildDots();
render();
restartAutoPlay();
