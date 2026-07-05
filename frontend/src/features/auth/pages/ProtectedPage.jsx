import { Button } from "@/components/ui/button";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { useGetCurrentUserQuery } from "../api/accountManagementApi";
import { useEffect, useState } from "react";
import { Badge } from "@/components/ui/badge";
import { useCreateRequestMutation, useGetRequestsQuery } from "../api/adminPromotionRequestApi";
import { Spinner } from "@/components/ui/spinner";
import { Skeleton } from "@/components/ui/skeleton";
import { useDispatch } from "react-redux";
import { logout } from "@/features/auth/authSlice";
import { useNavigate } from "react-router-dom";
import { api } from "@/app/api";

import AuthInformation from "../components/AuthInformation";
import RequestStatus from "../components/RequestStatus";
import PromotionRequests from "../components/PromotionRequests";
import CurrentAdmins from "../components/CurrentAdmins";
import ChangePassword from "../components/ChangePassword";

import { ArrowLeft, ArrowUpRight, LogOut, BadgeCheck, Key } from "lucide-react";
import { useSendEmailVerificationOtpMutation } from "../api/authApi";
import { toast } from "sonner";
import EmailVerification from "../components/EmailVerification";

const VIEWS = {
  HOME: "HOME",
  REQUEST_STATUS: "REQUEST_STATUS",
  PROMOTION_REQUESTS: "PROMOTION_REQUESTS",
  CURRENT_ADMINS: "CURRENT_ADMINS",
  CHANGE_PASSWORD: "CHANGE_PASSWORD",
  EMAIL_VERIFICATION: "EMAIL_VERIFICATION",
};

export default function ProtectedPage() {
  const { data: user, isLoading: isGettingUser } = useGetCurrentUserQuery();

  const { data: requests, isLoading: isGettingRequests } = useGetRequestsQuery();

  const [createRequest, { isLoading: isRequesting }] = useCreateRequestMutation();

  const [sendEmailVerificationOtp, { isLoading: isSendingOtp }] = useSendEmailVerificationOtpMutation();

  const [activeView, setActiveView] = useState(VIEWS.HOME);

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const isLoading = isGettingUser || isGettingRequests;

  const isSuperAdmin = user?.data?.highestRole === "ROLE_SUPER_ADMIN";

  const isUser = user?.data?.highestRole === "ROLE_USER";

  const hasRequests = (requests?.data?.length ?? 0) > 0;

  const isHome = activeView === VIEWS.HOME;

  const handleRequestAdminAccess = async () => {
    try {
      const response = await createRequest().unwrap();
      if (response.success) {
        setActiveView(VIEWS.REQUEST_STATUS);
        toast.success(response.message);
      }
    } catch (error) {
      toast.error(error?.data?.error?.message);
    }
  };

  const handleSendEmailVerificationOtp = async (email) => {
    try {
      const response = await sendEmailVerificationOtp({ email }).unwrap();
      if (response.success) {
        toast.info("Demo Mode: Email delivery is disabled in the deployed environment due to free-tier hosting limitations. The email workflow is implemented but actual emails are not sent in this demo.", {
          id: "email-demo",
          duration: 30000,
        });
        toast.success(response.message, { duration: 30000 });
        setActiveView(VIEWS.EMAIL_VERIFICATION);
      }
    } catch (error) {
      toast.error(error?.data?.error?.message);
    }
  };

  const handleBack = () => {
    if (activeView === VIEWS.CURRENT_ADMINS) {
      setActiveView(VIEWS.PROMOTION_REQUESTS);
      return;
    }
    setActiveView(VIEWS.HOME);
  };

  const renderContent = () => {
    switch (activeView) {
      case VIEWS.CHANGE_PASSWORD:
        return <ChangePassword setActiveView={setActiveView} />;

      case VIEWS.EMAIL_VERIFICATION:
        return <EmailVerification setActiveView={setActiveView} />;

      case VIEWS.REQUEST_STATUS:
        return <RequestStatus handleRequestAdminAccess={handleRequestAdminAccess} />;

      case VIEWS.PROMOTION_REQUESTS:
        return <PromotionRequests setActiveView={setActiveView} />;

      case VIEWS.CURRENT_ADMINS:
        return <CurrentAdmins />;

      default:
        return <AuthInformation />;
    }
  };

  return (
    <>
      {isLoading ? (
        <div className="flex min-h-screen items-center justify-center">
          <div className="w-md h-[404.8px] flex flex-col justify-between rounded-lg overflow-hidden border">
            <div className="h-1/4 w-full flex flex-wrap space-y-0.5 space-x-2.5 border-b rounded-lg p-5">
              <Skeleton className="h-7 w-25" />
              <Skeleton className="h-7 w-50" />
            </div>
            <div className="h-1/2 w-full space-y-2.5 p-5">
              <Skeleton className="h-7 w-full" />
              <Skeleton className="h-7 w-full" />
              <Skeleton className="h-7 w-full" />
              <Skeleton className="h-7 w-1/2" />
            </div>
            <div className="h-1/4 w-full flex justify-end items-end space-y-0.5 space-x-2.5 border-t rounded-lg p-5">
              <Skeleton className="h-7 w-50" />
              <Skeleton className="h-7 w-25" />
            </div>
          </div>
        </div>
      ) : (
        <div className="flex min-h-screen items-center justify-center">
          <Card className="mx-auto w-full max-w-md">
            <CardHeader className="flex flex-wrap space-y-0.5 space-x-0.5 items-center">
              <Badge className="bg-blue-50 text-blue-700 dark:bg-blue-950 dark:text-blue-300 border-2 border-blue-100 h-7 font-extralight text-lg mb-1">{user?.data?.username}</Badge>

              <Badge className="bg-blue-50 text-blue-700 dark:bg-blue-950 dark:text-blue-300 border-2 border-blue-100 h-7 font-extralight text-lg mb-1">{user?.data?.highestRole}</Badge>

              {user?.data?.emailVerified ? (
                <Badge className="bg-emerald-50 text-emerald-700 dark:bg-emerald-950 dark:text-emerald-300 border-2 border-emerald-100 h-7 font-extralight text-lg mb-1">
                  <span className="text-emerald-500">
                    <BadgeCheck size={16} />
                  </span>
                  Email verified
                </Badge>
              ) : (
                activeView !== VIEWS.EMAIL_VERIFICATION && (
                  <Badge onClick={() => handleSendEmailVerificationOtp(user?.data?.email)} className="bg-red-50 text-red-600 dark:bg-red-950 dark:text-red-300 border-2 border-red-100 h-7 font-extralight text-lg mb-1 cursor-pointer">
                    Verify email
                    {isSendingOtp ? (
                      <Spinner data-icon="inline-start" />
                    ) : (
                      <span className="text-red-400">
                        <ArrowUpRight size={16} />
                      </span>
                    )}
                  </Badge>
                )
              )}

              {user?.data?.provider === "LOCAL" && activeView !== VIEWS.CHANGE_PASSWORD && (
                <Badge onClick={() => setActiveView(VIEWS.CHANGE_PASSWORD)} className="bg-blue-50 text-blue-700 dark:bg-blue-950 dark:text-blue-300 border-2 border-blue-100 h-7 font-extralight text-lg mb-1 cursor-pointer">
                  Change password
                  <span className="text-blue-500">
                    <Key size={16} />
                  </span>
                </Badge>
              )}
            </CardHeader>

            <CardContent className="-mb-(--card-spacing)">{renderContent()}</CardContent>

            <CardFooter className="justify-end gap-2">
              {isUser && !hasRequests && isHome && (
                <Button onClick={handleRequestAdminAccess} disabled={isRequesting}>
                  Request admin access
                  {isRequesting ? <Spinner data-icon="inline-start" /> : <ArrowUpRight />}
                </Button>
              )}

              {!isSuperAdmin && hasRequests && isHome && (
                <Button onClick={() => setActiveView(VIEWS.REQUEST_STATUS)}>
                  View request status
                  <ArrowUpRight />
                </Button>
              )}

              {isSuperAdmin && isHome && (
                <Button onClick={() => setActiveView(VIEWS.PROMOTION_REQUESTS)}>
                  View promotion requests
                  <ArrowUpRight />
                </Button>
              )}

              {!isHome && (
                <Button onClick={handleBack}>
                  <ArrowLeft />
                  Go back
                </Button>
              )}

              <Button
                variant="destructive"
                onClick={() => {
                  dispatch(logout());
                  dispatch(api.util.resetApiState());

                  navigate("/auth/login", {
                    replace: true,
                  });
                }}
              >
                Logout
                <LogOut />
              </Button>
            </CardFooter>
          </Card>
        </div>
      )}
    </>
  );
}
