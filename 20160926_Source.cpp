#include <GLFW/glfw3.h> 
#include <cstring> 
#include <stdlib.h>		  // srand, rand 
#include <thread>         // std::this_thread::sleep_for 
#include <chrono>         // std::chrono::seconds 
#include "math.h" 
#include <windows.h> 
#include <stdbool.h> 
#define pi 3.14159265358979 

	const int width = 1366; //캔버스 가로
	const int height = 768; //캔버스 세로

 
	float* pixels = new float[width*height * 3];// 픽셀의 배열 선언. 해상도 x 3(rgb)입니다. 
	double xpos, ypos;//마우스 커서의 위치를 저장할 변수 ( xpos, ypos)
 
/*마우스 커서가 바깥 원 내에 있는지를 판단하기 위한 함수*/
bool incircle(const int& i,const int& j,const int& r)//마우스 커서가 바깥 원 내에 있는지를 판단하기 위한 함수
{ 
	if ((xpos - i)*(xpos - i) + (ypos - j)*(ypos - j) <= r*r)//내부이면 
	{ 
		return true; 
 	} 
 	else //외부이면
 	{ 
 		return false; 
 	} 
}

/*픽셀배열에 값(rgb)을 넣는 함수 */
void drawPixel(const int& i, const int& j, const float& red, const float& green, const float& blue)
{ 
 	pixels[(i + width* j) * 3 + 0] = red; 
 	pixels[(i + width* j) * 3 + 1] = green; 
 	pixels[(i + width* j) * 3 + 2] = blue; 
}  
 
/*두 점을 이어주는 직선을 그리는 함수*/
void drawLine(const int& i0, const int& j0, const int& i1, const int& j1, const float& red, const float& green, const float& blue) 
{  
 	if (i1 - i0 == 0)//직선 함수는 분모가 0이면 즉 x2-x1 차이가 0이면 오류가 나므로 0일때 y축만그리는것으로함. 
 	{ 
 		for (int i = j0;i < j1;i++) 
 		{ 
 			drawPixel(i1, i, red, green, blue); 
 		} 
 	} 
 	else if ((j1 - j0) != 0)//기울기가 0이아닌 나머지일경우  
 	{ 
 		for (int i = i0; i < i1; i++) 
 		{ 
 			const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0; 
 			drawPixel(i, j, red, green, blue);// i부터 j까지 rgb값으로 그린다 
 			drawPixel(i, j - 1, red, green, blue);drawPixel(i, j + 1, red, green, blue);//기울어지면 픽셀수가 적어지기때문에 추가해주었습니다. 
		} 
 	} 
 	else//가로일경우  
 	{ 
 		for (int i = i0; i < i1; i++) 
 		{ 
 			const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0; 
 			drawPixel(i, j, red, green, blue);// i부터 j까지 rgb값으로 그린다 
 		} 
 	} 
} 
 
/*1픽셀 이상의 두꺼운 직선을 그리는 함수*/
void thickLine(const int& i0, const int& j0, const int& i1, const int& j1, const float& red, const float& green, const float& blue) 
{ 
 	if (i1 - i0 == 0)//세로그리기 
 	{ 
 		for (int i = j0;i < j1;i++) 
 		{ 
 			for (int j = 0;j < 10;j++)//기존픽셀에서 x값을 10만큼 증가시켜 찍습니다.(두꺼워지는과정) 
 			{ 
 				drawPixel(i1 + j, i, red, green, blue); 
 			} 
 		} 
 	} 
 	else if ((j1 - j0) != 0) 
 	{ 
 		for (int i = i0; i < i1; i++) 
 		{ 
 			const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0; 
 			for (int k = 0;k < 10;k++) 
 			{ 
 				drawPixel(i, j, red, green, blue);// i부터 j까지 rgb값으로 그린다 
 				drawPixel(i, j - k, red, green, blue);drawPixel(i, j + k, red, green, blue); 
 			} 
 		} 
 	} 
 	else 
 	{ 
 		for (int i = i0; i < i1; i++) 
 		{ 
 			const int j = (j1 - j0)*(i - i0) / (i1 - i0) + j0; 
  			drawPixel(i, j, red, green, blue);// i부터 j까지 rgb값으로 그린다 
  		} 
 	} 
} 
 
/*원을 그리는 함수*/
void drawcircle(const int& x1, const int& y1, const int& r, const float& red, const float& green, const float& blue) 
{ 
 	float i = 0.0; 
 	float rad_to_deg = 0.0; 
 	float degree = 360.0; 
 	int x2 = 0, y2 = 0;  
  	 
 	for (degree = 0;degree < 360;degree++) 
 	{ 
 		rad_to_deg = degree*(pi / 180); 
 		x2 = x1 + r*sin(rad_to_deg); 
 		y2 = y1 + r*cos(rad_to_deg); 
 		drawPixel(x2, y2, red, green, blue); 
 		drawPixel(x2 + 1, y2, red, green, blue); 
 		drawPixel(x2, y2 - 1, red, green, blue); 
 	} 
 
} 

/*안이 빈 사각형 그리기*/
void drawsqaure() 
{ 
 	drawLine(650, 600, 750, 600, 1.0f, 0.0f, 0.0f); 
 	drawLine(650, 500, 650, 600, 1.0f, 0.0f, 0.0f); 
 	drawLine(650, 500, 750, 500, 1.0f, 0.0f, 0.0f); 
 	drawLine(750, 500, 750, 601, 1.0f, 0.0f, 0.0f);  
} 

/*삼각형 그리기*/ 
void drawtriangle() 
{ 
 	drawLine(100, 100, 200, 250, 1.0f, 0.0f, 0.0f); 
 	drawLine(100, 100, 300, 100, 1.0f, 0.0f, 0.0f); 
 	drawLine(200, 250, 300, 100, 1.0f, 0.0f, 0.0f); 
 } 

void drawOnPixelBuffer() 
{ 
 	//std::memset(pixels, 1.0f, sizeof(float)*width*height * 3); // doesn't work 
 	std::fill_n(pixels, width*height * 3, 1.0f);	// white background 
} 

void draw() 
{ 
 	float r,g,b=0.0f; 
 	ypos = height - ypos; 
 	printf("xpos =%lf ypos= %lf\n", xpos, ypos); 
 	const int i_center = 800, j_center = 450; 
 	const int thickness = 100; 

 
 	drawsqaure(); 
 	thickLine(150, 500, 250, 600, 1.0f, 0.0f, 0.0f); 
 	drawcircle(450, 550,30 , 1.0f, 0.0f, 0.0f); 	//x자  
	drawLine(900, 500, 1000, 600, 1.0f, 0.0f, 0.0f); 
 	drawLine(900, 600, 1000, 500, 1.0f, 0.0f, 0.0f); 	//x자 
 
 	drawLine(1200, 500, 1200, 600, 1.0f, 0.0f, 0.0f); 
 	drawLine(1170, 530, 1200, 500, 1.0f, 0.0f, 0.0f); 
 	drawLine(1200, 500, 1230, 530, 1.0f, 0.0f, 0.0f);	//down arrow 
 
 
	drawLine(150, 200, 250, 200, 1.0f, 0.0f, 0.0f); 
 	drawLine(230, 230, 250, 200, 1.0f, 0.0f, 0.0f); 
 	drawLine(230, 170, 250, 200, 1.0f, 0.0f, 0.0f);  	// right arrow 
 	
	drawLine(400, 160, 450, 260, 1.0f, 0.0f, 0.0f); 
 	drawLine(450, 260, 500, 160, 1.0f, 0.0f, 0.0f); 
	drawLine(420, 200, 480, 200, 1.0f, 0.0f, 0.0f);  	// draw 'A' 
 
 
 	drawLine(700, 160, 700, 260, 1.0f, 0.0f, 0.0f);  	//draw l 
 	drawLine(900, 200, 1000, 200, 1.0f, 0.0f, 0.0f); 
 	drawLine(900, 200, 930, 230, 1.0f, 0.0f, 0.0f); 
 	drawLine(900, 200, 930, 170, 1.0f, 0.0f, 0.0f);  	//draw left arrow 
 	drawLine(1200, 160, 1200, 260, 1.0f, 0.0f, 0.0f); 
 	drawLine(1170, 230, 1200, 260, 1.0f, 0.0f, 0.0f); 
 	drawLine(1200, 260, 1230, 230, 1.0f, 0.0f, 0.0f); 
 	for (int i = 200;i < 768;i += 350) 
 	{	 
 		for (int j = 200;j < 1366;j+=250) 
 		{ 
 			if (incircle(j,i,100)== true) 
 			{ 
 				r = 0.0f; 
 				g = 0.0f; 
 				b = 1.0f; 
 			} 
 			else 
 			{ 
 				r = 0.0f; 
				g = 0.0f; 
 				b = 0.0f; 
 			} 
 				drawcircle(j, i, 100, r, g, b); 
 			 
 		} 
 	} 
} 

int main(void) 
{ 
 	GLFWwindow* window;  	 
 	/* Initialize the library */ 
 	if (!glfwInit()) 
 		return -1;  
 
 	/* Create a windowed mode window and its OpenGL context */ 
 	window = glfwCreateWindow(width, height, "Hello World", NULL, NULL); 
 	if (!window) 
 	{ 
 		glfwTerminate(); 
 		return -1; 
 	} 
 
  	/* Make the window's context current */ 
 	glfwMakeContextCurrent(window); 
 	glClearColor(1, 1, 1, 1); // while background  
 
 							  /* Loop until the user closes the window */ 
 	while (!glfwWindowShouldClose(window)) 
 	{ 
		glfwGetCursorPos(window, &xpos, &ypos); 
 		 
 		/* Render here */ 
 		glClear(GL_COLOR_BUFFER_BIT); 
 		drawOnPixelBuffer(); 
		//TODO: RGB struct 
 
		//Make a pixel drawing function 
		//Make a line drawing function 
 		draw(); 
 		glDrawPixels(width, height, GL_RGB, GL_FLOAT, pixels);  
 
 		/* Swap front and back buffers */ 
 		glfwSwapBuffers(window); 
 
 
 		/* Poll for and process events */ 
 		glfwPollEvents(); 
 
 		std::this_thread::sleep_for(std::chrono::milliseconds(100)); 
 	} 
 
 
 	glfwTerminate(); 
  	delete[] pixels; // or you may reuse pixels array   
 
 	return 0; 
} 
