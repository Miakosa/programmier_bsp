#pragma once

#include <Windows.h>
/* define Window prosedure
* HWND : which Wiondow it comes from
* UINT : the message
* WPARAM : Info about message
* LPARAM : "
*/ 

LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam);


class Window
{
public :
		// default Constructor
		Window();
		// delete Copy-Constuructor so the Window can be duplicated
		Window(const Window&) = delete;
		// also equals-operator
		Window& operator = (const Window&) = delete;
		// Destructor for the Window (?)
		~Window();

		//
		bool ProcessMessages();

private:
	// both required when creating a Window
	// connected to application
	HINSTANCE m_hInstance;
	// connected to actuall Window instance
	HWND mhWnd;

};

