import { Card, CardContent, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import GoogleLoginButton from "../components/GoogleLoginButton";
import { Button } from "@/components/ui/button";

const AuthLayout = () => {
  const { pathname } = useLocation();
  const navigate = useNavigate();

  const page = pathname.split("/").pop();

  return (
    <div className="flex min-h-screen items-center justify-center">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>{page && page.replace("-", " ").charAt(0).toUpperCase() + page.replace("-", " ").slice(1)}</CardTitle>
        </CardHeader>

        <CardContent className="h-45">
          <Outlet />
        </CardContent>

        <CardFooter className="flex flex-col gap-4">
          {page === "login" && (
            <>
              <Button variant="outline" className="w-full" onClick={() => navigate("/auth/register")}>
                Don't have an account?
              </Button>
            </>
          )}

          {page === "register" && (
            <Button variant="outline" className="w-full" onClick={() => navigate("/auth/login")}>
              Already have an account?
            </Button>
          )}

          {page === "forgot-password" && (
            <Button variant="outline" className="w-full" onClick={() => navigate("/auth/login")}>
              Back to login
            </Button>
          )}

          {page === "reset-password" && (
            <Button variant="outline" className="w-full" onClick={() => navigate("/auth/forgot-password")}>
              Use another email
            </Button>
          )}

          {/* Render once */}
          <GoogleLoginButton />
        </CardFooter>
      </Card>
    </div>
  );
};

export default AuthLayout;
