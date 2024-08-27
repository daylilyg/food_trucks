import { RouteObject } from "react-router-dom";
import Layout from "./components/Layout";
import MainPage from './views/MainPage'


export const routes: RouteObject[] = [
  {
    element: <Layout />,
    children: [
      {
        path: "/",
        element: <MainPage />,
      }
    ],
  },
];
