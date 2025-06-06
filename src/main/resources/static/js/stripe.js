const stripe = Stripe('pk_test_51RTiYLB1nhrCgbjCIUnEeX8OuCxOX9KYbylADBbNmsr2htMP5UvMuYRgHQWFldm82m7ARKMzsfdx7e06YYbmiTHA00kufUVjWi');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
 stripe.redirectToCheckout({
   sessionId: sessionId
 })
});