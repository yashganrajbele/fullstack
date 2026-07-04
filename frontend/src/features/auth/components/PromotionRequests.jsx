import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ButtonGroup } from "@/components/ui/button-group";
import { useState } from "react";
import { useApproveRequestMutation, useGetPendingRequestsQuery, useRejectRequestMutation } from "../api/adminPromotionRequestApi";
import { Spinner } from "@/components/ui/spinner";
import { ArrowUpRight } from "lucide-react";
import { Skeleton } from "@/components/ui/skeleton";

const PromotionRequests = ({ setActiveView }) => {
  const { data: pendingRequests, isLoading: isGettingPendingRequests, error: getPendingRequestsError } = useGetPendingRequestsQuery();
  const [rejectRequest, { isLoading: isRejecting }] = useRejectRequestMutation();
  const [approveRequest, { isLoading: isApproving }] = useApproveRequestMutation();
  const [isRejectingRequestId, setIsRejectingRequestId] = useState(null);
  const [isApprovingRequestId, setIsApprovingRequestId] = useState(null);

  const handleRejectRequest = async (requestId) => {
    try {
      setIsRejectingRequestId(requestId);
      const response = await rejectRequest(requestId).unwrap();
      if (response.success) {
        toast.success(response.message || "Request rejected successfully");
      }
    } catch (error) {
      toast.error("Failed to reject request. Please try again.");
    } finally {
      setIsRejectingRequestId(null);
    }
  };

  const handleApproveRequest = async (requestId) => {
    try {
      setIsApprovingRequestId(requestId);
      await approveRequest(requestId).unwrap();
    } catch (error) {
      console.log("Failed to approve request");
    } finally {
      setIsApprovingRequestId(null);
    }
  };

  const getPendingRequestsClasses = () => {
    return "bg-blue-50 text-blue-400 border-blue-100 dark:bg-blue-950 dark:text-blue-300";
  };

  const getPendingRequestsButtonClasses = (action) => {
    switch (action) {
      case "REJECT":
        return "bg-red-100 hover:bg-red-200 text-red-400 hover:text-red-500 border-red-300 hover:border-red-400 dark:bg-red-900 dark:text-red-200";

      case "APPROVE":
        return "bg-emerald-100 hover:bg-emerald-200 text-emerald-400 hover:text-emerald-500 border-emerald-300 hover:border-emerald-400 dark:bg-emerald-900 dark:text-emerald-200";

      default:
        return "";
    }
  };

  return (
    <div className="relative">
      <div className="-mx-(--card-spacing) h-60 max-h-60 space-y-2.5 overflow-y-auto border-t px-(--card-spacing) py-4">
        {isGettingPendingRequests ? (
          [1].map((key) => <Skeleton key={key} className="w-full h-[67.2px]" />)
        ) : pendingRequests?.data?.length ? (
          pendingRequests.data.map((request) => (
            <div key={request.requestId} className={`rounded-xl border-2 px-5 py-2.5 transition-all duration-200 hover:opacity-90 ${getPendingRequestsClasses()}`}>
              <div className="flex justify-between items-center w-full space-y-1">
                <div className="flex flex-col">
                  <span className="font-medium">{request.username}</span>
                  <span className="opacity-75">{new Date(request.createdAt).toLocaleString()}</span>
                </div>

                <ButtonGroup className="">
                  <Button onClick={() => handleApproveRequest(request.requestId)} disabled={isApproving || isRejecting} className={`font-medium leading-relaxed tracking-wide ${getPendingRequestsButtonClasses("APPROVE")}`}>
                    Approve
                    {isApprovingRequestId === request.requestId ? <Spinner data-icon="inline-start" /> : ""}
                  </Button>
                  <Button onClick={() => handleRejectRequest(request.requestId)} disabled={isRejecting || isApproving} className={`font-medium leading-relaxed tracking-wide ${getPendingRequestsButtonClasses("REJECT")}`}>
                    Reject
                    {isRejectingRequestId === request.requestId ? <Spinner data-icon="inline-start" /> : ""}
                  </Button>
                </ButtonGroup>
              </div>
            </div>
          ))
        ) : (
          <div className="flex h-32 items-center justify-center text-sm text-muted-foreground">No requests found.</div>
        )}
      </div>
      <Button onClick={() => setActiveView("CURRENT_ADMINS")} className="absolute bottom-4 right-4 z-10">
        View current admins
        <ArrowUpRight />
      </Button>
    </div>
  );
};

export default PromotionRequests;
