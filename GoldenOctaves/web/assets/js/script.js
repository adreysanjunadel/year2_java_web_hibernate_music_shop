function showEditProfile() {
  const accountDetailsSection = document.getElementById("accountDetails");
  const editProfileForm = document.getElementById("editProfileForm");

  accountDetailsSection.style.display = "none";
  editProfileForm.style.display = "block";
}

function validateInteger(input) {
    // Remove any non-numeric characters, except for negative sign and decimal point
    input.value = input.value.replace(/[^0-9]/g, '');
}

document.addEventListener('DOMContentLoaded', function () {
    const topStrip = document.getElementById('top-strip');
    const floatingHeader = document.getElementById('floating-header'); // Ensure the correct ID
    const floatingIsland = document.querySelector('.floating-island');
    let lastScrollTop = 0;

    function adjustHeaderOnScroll() {
        let scrollTop = window.pageYOffset || document.documentElement.scrollTop;

        if (scrollTop === 0) {
            // User is at the very top of the page
            topStrip.style.top = '0'; // Show the top strip
            floatingHeader.style.top = '5.75%'; // Adjusted gap when at the top
            floatingIsland.classList.add('at-top'); // Add shadow when at the top
        } else if (scrollTop > lastScrollTop) {
            // User is scrolling down - hide the top strip and keep the island near the top
            topStrip.style.top = '-50px'; // Adjust to hide the top strip
            floatingHeader.style.top = '1%'; // Adjusted gap when scrolling down
            floatingIsland.classList.remove('at-top'); // Remove the shadow class when scrolling
        } else if (scrollTop < lastScrollTop && scrollTop > 0) {
            // User is scrolling up but not at the top - don't show the top strip
            topStrip.style.top = '-50px'; // Keep it hidden
            floatingHeader.style.top = '1%'; // Maintain the reduced gap when scrolling up
            floatingIsland.classList.remove('at-top'); // Ensure shadow is removed
        }

        // Update last scroll position
        lastScrollTop = scrollTop;
    }

    // Initial check when page loads
    adjustHeaderOnScroll();

    // Scroll event listener
    window.addEventListener('scroll', adjustHeaderOnScroll);
});

document.addEventListener('DOMContentLoaded', function () {
  const carouselElement = document.querySelector('#hot-deals-carousel');
  const carousel = new bootstrap.Carousel(carouselElement, {
    interval: 5000 // This line can be used to set the default interval in the carousel options (optional)
  });

  const dots = document.querySelectorAll('.carousel-dots button');

  carouselElement.addEventListener('slide.bs.carousel', function (event) {
    const activeIndex = event.to;
    dots.forEach((dot, index) => {
      dot.classList.toggle('active', index === activeIndex);
    });

    // Update active class on slide change
    const allCarouselItems = carouselElement.querySelectorAll('.carousel-item');
    allCarouselItems.forEach(item => item.classList.remove('active'));
    const activeItem = allCarouselItems[activeIndex];
    activeItem.classList.add('active');
  });
});

async function checkSignedInUser() {
  const response = await fetch("CheckSignedInUserDetails");

  const popup = Notification(); // Assuming Notification is a custom popup function

  if (response.ok) {
    const json = await response.json();
    console.log(json); // Log the response for debugging

    const response_dto = json.response_dto;

    if (response_dto && response_dto.success) {
      // Signed In
      const user = response_dto.content;

      const st_quick_link = document.getElementById("st-quick-link");

      // Ensure elements exist before modification
      const st_quick_link_li_1 = document.getElementById("st-quick-link-li-1");
      if (st_quick_link_li_1) {
        st_quick_link_li_1.remove(); // Remove existing "Join Us" element
      }

      const st_quick_link_li_2 = document.getElementById("st-quick-link-li-2");
      if (st_quick_link_li_2) {
        st_quick_link_li_2.remove(); // Remove existing "Sign In" element
      }

      // Create new list items for user name
      const new_li_tag1 = document.createElement("li");
      new_li_tag1.className = "list-inline-item"; // Add class for styling
      const new_li_a_tag1 = document.createElement("a");
      new_li_a_tag1.href = "#";
      new_li_a_tag1.textContent = user.first_name + " " + user.last_name;
      new_li_tag1.appendChild(new_li_a_tag1);
      st_quick_link.appendChild(new_li_tag1);
    } else {
      // Not Signed In
      // You can handle this case by adding appropriate messages or links
    }
  } else {
    popup.error({ message: "Unable to process your request!" });
  }
}







