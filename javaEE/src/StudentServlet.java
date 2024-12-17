    import jakarta.servlet.ServletConfig;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.annotation.WebServlet;
    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;

    import java.io.IOException;
    import java.io.PrintWriter;
    import java.util.ArrayList;
    import java.util.List;

    @WebServlet(urlPatterns = "/student")
    public class StudentServlet extends HttpServlet {
        private final List<StudentDTO> stList= new ArrayList<>();

        @Override
        public void init(ServletConfig config) throws ServletException {
            stList.add(new StudentDTO("1","dila", "dila@gmail.com", 20));
            stList.add(new StudentDTO("2","nila", "nila@gmail.com", 22));
            stList.add(new StudentDTO("3","fila", "fila@gmail.com", 21));
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String stringage = req.getParameter("age");

            int id = stList.size() + 1;
            int age = Integer.parseInt(req.getParameter("age"));

            StudentDTO studentDTO = new StudentDTO(String.valueOf(id), name, email, age);
            stList.add(studentDTO);
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter printWriter = resp.getWriter();
            StringBuilder sb = new StringBuilder();
            resp.setContentType("application/json");

            sb.append("[");

            for (int i = 0; i < stList.size(); i++) {
                StudentDTO st = stList.get(i);
                String json = String.format("{\"id\": \"%s\", \"name\": \"%s\", \"email\": \"%s\", \"age\": %d}"
                        ,st.getId(), st.getName(), st.getEmail(), st.getAge());


                sb.append(json);

                if (i < stList.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            String jlist = sb.toString();
            printWriter.write(jlist);

        }

        @Override
        protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentType("application/json");

            try {
                String id = req.getParameter("id");
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                String age = req.getParameter("age");

                if (id == null || name == null || email == null || age == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"message\": \"Invalid input\"}");
                    return;
                }

                int studentId = Integer.parseInt(id);
                int studentAge = Integer.parseInt(age);
                StudentDTO dto = new StudentDTO(id, name, email, studentAge);

                for (int i = 0; i < stList.size(); i++) {
                    if (stList.get(i).getId().equals(id)) {
                        stList.set(i, dto);
                        break;
                    }}




            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"message\": \"Invalid age format\"}");
                e.printStackTrace();
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"message\": \"An error occurred while updating the student\"}");
                e.printStackTrace();
            }
        }

        @Override
        protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String studentId = req.getParameter("id");
            System.out.println(studentId);

            if (studentId == null || studentId.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Invalid student ID\"}");
                return;
            }

            boolean studentFound = false;

            for (int i = 0; i < stList.size(); i++) {
                if (stList.get(i).getId().equals(studentId)) {
                    stList.remove(i);
                    studentFound = true;
                    break;
                }
            }
            resp.setContentType("application/json");

            if (studentFound) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"Student deleted successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Student not found\"}");
            }
        }
    }
