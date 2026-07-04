import React, { useEffect } from "react";
import AppRoutes from "./app/routes/AppRoutes";
import { Toaster } from "./components/ui/sonner";
import { toast } from "sonner";

const App = () => {
  useEffect(() => {
    if (window.innerWidth < 1024) {
      toast.info("For the best experience, please open this application on a desktop or laptop", {
        id: "desktop-warning",
      });
    }
  }, []);

  return (
    <>
      <AppRoutes />
      <Toaster
        position="top-center"
        duration={7500}
        toastOptions={{
          classNames: {
            error: "!bg-red-100 !text-red-500 !border-red-300",
            info: "!bg-blue-100 !text-blue-500 !border-blue-300",
            success: "!bg-emerald-100 !text-emerald-500 !border-emerald-300",
          },
        }}
      />
    </>
  );
};

export default App;
