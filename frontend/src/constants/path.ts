export const ROUTER_PATH = {
  common: {
    path: "/",
    name: "common",
    children: {
      login: {
        path: "login",
        name: "Login",
      },
      unauthorized: {
        path: "unauthorized",
        name: "Unauthorized",
      },
      forbidden: {
        path: "forbidden",
        name: "Forbidden",
      },
      notfound: {
        path: "*",
        name: "Notfound",
      },
    },
  },
  admin: {
    path: "/admin",
    name: "admin",
    children: {
      dashboard: {
        path: "dashboard",
        name: "admin-dashboard",
      },
      skills: {
        path: "skills",
        name: "admin-skills",
      },
      templates: {
        path: "templates",
        name: "admin-templates",
      },
      users: {
        path: "users",
        name: "admin-users",
      },
      questions: {
        path: "questions",
        name: "admin-questions",
      },
    },
  },
  interviewer: {
    path: "/interviewer",
    name: "interviewer",
    children: {
      session: {
        path: "session",
        name: "interviewer-session",
      },
    },
  },
};
