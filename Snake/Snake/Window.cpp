#include "Window.h"
#include "resource.h"
#include <tchar.h>


LRESULT WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
    // paint a text into the window
    PAINTSTRUCT ps;
    HDC hdc;
    TCHAR greeting[] = _T("Erstes Fenster");
    HICON hIcon1 = LoadIcon(NULL, MAKEINTRESOURCE(108));

    switch (uMsg)
    {
    // paint in window
    case WM_PAINT:
        hdc = BeginPaint(hwnd, &ps);
        // print text top left corner
        TextOut(hdc, 5, 5, greeting, _tcslen(greeting));
        DrawIcon(hdc, 30, 20, hIcon1);
        // End layout section
        EndPaint(hwnd, &ps);
        break;
        // Window closed
    case WM_CLOSE:
        DestroyWindow(hwnd);
        break;
    //Window destroyed -> quit application
    case WM_DESTROY:
        PostQuitMessage(0);
        return 0;
    }
  
    // handles whjat we did not define
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}

// creating Window
Window::Window()
// initialise HInstance to function getModuleHandle
    :m_hInstance(GetModuleHandle(nullptr))
{
    /*
    *  wide char pointer
    *  L defines a wide String
    *  
    */

    const wchar_t* CLASS_NAME = L"Kola's erstes Fenster";
   
  
  
    WNDCLASS wndClass = {};
    wndClass.lpszClassName = CLASS_NAME;
    wndClass.hInstance = m_hInstance;
    wndClass.hIcon = LoadIcon(m_hInstance, MAKEINTRESOURCE(108)); // other Icons possible
    wndClass.hCursor = LoadCursor(NULL, IDI_HAND);
    wndClass.hbrBackground = CreatePatternBrush(LoadBitmap(m_hInstance, MAKEINTRESOURCE(107)));
    // Window procedure
    wndClass.lpfnWndProc = WindowProc; // Function Pointer

    // register Windowclass ,a reference to wndClass object
    RegisterClass(&wndClass);

    // combine with bitwise oparator 
    /* Caption : Titlebox
    * Mini : creates a mini box
    * Sys : close-icon, box , ..
    */
    DWORD style = WS_CAPTION | WS_MINIMIZEBOX | WS_SYSMENU;
    
    // window size
    int width = 800;
    int height = 800;


    // dimentions of the Window
    RECT rect;
    rect.left = 600; // left and top = possition window "starts"
    rect.top = 70;
    rect.right = rect.left + width;
    rect.bottom = rect.top + height;
  
    // -> created window where the outside border is that size
    // adjust so the "canvas" is the set size
    AdjustWindowRect(&rect, style, false);

    mhWnd = CreateWindowEx(
        0,
        CLASS_NAME,
        L"Tite",
        style,
        rect.left,
        rect.top,
        // not width and height because we want to use the adjusted width and height
        rect.right - rect.left,
        rect.bottom - rect.top,
        NULL,
        NULL,
        m_hInstance,
        NULL
    );
    ShowWindow(mhWnd, SW_SHOW);
    UpdateWindow(mhWnd);
}

// 
Window::~Window()
{
    // un-register Class after closing
    const wchar_t* CLASS_NAME = L"Kola's erstes Fenster";

    UnregisterClass(CLASS_NAME, m_hInstance);

}

bool Window::ProcessMessages()
{
    MSG msg = {};
    // PM removes message after we got it
    while (PeekMessage(&msg, nullptr, 0u, 0u, PM_REMOVE))
    {
        if (msg.message == WM_QUIT)// Quit  when PostQuitMessage(0) is send
        {
            return false;
        }
        // translation keypresses into characters
        TranslateMessage(&msg);
        // causes  window procedure
        DispatchMessage(&msg);
    }
    // true as long as Programm is running
    return true;
}

