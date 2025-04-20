cmake_minimum_required(VERSION 3.16)
project(Snake VERSION 3.0 LANGUAGES CXX C)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)
set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(PDCURSES_DIR ${CMAKE_SOURCE_DIR}/src/main/java/ui/tui/pdcurses)

find_package(Qt5 REQUIRED COMPONENTS Widgets)
find_package(OpenGL REQUIRED)

set(COMMON_SOURCES
    src/main/java/backend/snake/model.cpp
    src/main/java/backend/snake/snake.cpp
    src/main/java/backend/snake/snake_part.cpp
)

set(MAIN_SOURCES
    src/main/java/backend/snake/gui_controller.cpp
    src/main/java/backend/snake/web_controller.cpp
    src/main/java/backend/snake/game.cpp
    src/main/java/backend/snake/main.cpp
    src/main/java/ui/gui/gui_snake.cpp
    ${COMMON_SOURCES}
)

set(TUI_SOURCES
    src/main/java/backend/snake/tui_launcher.cpp
    src/main/java/backend/snake/tui_controller.cpp
    src/main/java/ui/tui/tui_snake.cpp
    ${COMMON_SOURCES}
)

add_executable(Snake ${MAIN_SOURCES})
target_include_directories(Snake PRIVATE
    ${PDCURSES_DIR}
    ${CMAKE_SOURCE_DIR}/backend/snake
    ${Qt5Widgets_INCLUDE_DIRS}
)

add_executable(TuiSnake ${TUI_SOURCES})
target_include_directories(TuiSnake PRIVATE
    ${PDCURSES_DIR}
    ${CMAKE_SOURCE_DIR}/backend/snake
)

if(WIN32)
    set_target_properties(Snake PROPERTIES
        WIN32_EXECUTABLE FALSE
    )
    
    set_target_properties(TuiSnake PROPERTIES
        WIN32_EXECUTABLE FALSE
        LINK_FLAGS "-mconsole"
    )
endif()

if(WIN32)
    add_definitions(
        -D_WIN32_WINNT=0x0601
        -DWIN32_LEAN_AND_MEAN
        -DNOMINMAX
    )

    add_custom_command(TARGET Snake POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy
            ${PDCURSES_DIR}/pdcurses.dll
            $<TARGET_FILE_DIR:Snake>
    )
    
    add_custom_command(TARGET TuiSnake POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy
            ${PDCURSES_DIR}/pdcurses.dll
            $<TARGET_FILE_DIR:TuiSnake>
    )
    
    target_link_libraries(Snake PRIVATE
        Qt5::Widgets
        OpenGL::GL
        ws2_32
        wsock32
        
    )
    
    target_link_libraries(TuiSnake PRIVATE
        ${PDCURSES_DIR}/pdcurses.a
        ws2_32
        wsock32
    )
endif()

set_target_properties(Snake PROPERTIES
    MACOSX_BUNDLE TRUE
    MACOSX_BUNDLE_GUI_IDENTIFIER "my.example.com"
    MACOSX_BUNDLE_BUNDLE_VERSION ${PROJECT_VERSION}
    MACOSX_BUNDLE_SHORT_VERSION_STRING ${PROJECT_VERSION_MAJOR}.${PROJECT_VERSION_MINOR}
)