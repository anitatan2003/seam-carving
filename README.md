# Photo seam carving

Seam carving is a technique for image resizing, where the size of an image is reduced by one pixel in height (by removing a horizontal seam) or width (by removing a vertical seam) at a time. Unlike cropping pixels from the edges or scaling the entire image, seam carving is content-aware, because it attempts to identify and preserve the most important content in an image by inferring the “importance” of each pixel from the surrounding pixels. Seam carving works by using an energy function to find the lowest energy-connected pixels either horizontally or vertically across an image and then removing it. 

Cropping an image into a square:

<img width="630" alt="Screenshot 2025-05-10 at 10 23 54 PM" src="https://github.com/user-attachments/assets/5d7970ce-a3d9-4acc-868c-ce7a09698149" />
