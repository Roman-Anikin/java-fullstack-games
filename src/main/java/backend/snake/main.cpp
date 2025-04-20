#include "web_controller.h"
#include <iostream>

using namespace s21;

int main() {
    std::cout << "Server starting on port 8081..." << std::endl;
    WebController controller;
    httplib::Server server;
    controller.registerEndpoints(server);
    std::cout << "Press Ctrl+C to stop server" << std::endl;
    server.listen("0.0.0.0", 8081);
    return 0;
}