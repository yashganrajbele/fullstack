import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Spinner } from "@/components/ui/spinner";
import { useCreateRequestMutation, useGetRequestsQuery } from "../api/adminPromotionRequestApi";
import { ArrowUpRight } from "lucide-react";

const RequestStatus = ({ handleRequestAdminAccess }) => {
  const [{ isLoading: isRequesting }] = useCreateRequestMutation();
  const { data: requests } = useGetRequestsQuery();

  const getStatusClasses = (status) => {
    switch (status) {
      case "PENDING":
        return "bg-yellow-50 text-yellow-500 border-yellow-200 dark:bg-yellow-950 dark:text-yellow-300";

      case "REJECTED":
        return "bg-red-50 text-red-500 border-red-200 dark:bg-red-950 dark:text-red-300";

      case "APPROVED":
        return "bg-emerald-50 text-emerald-500 border-emerald-200 dark:bg-emerald-950 dark:text-emerald-300";

      default:
        return "bg-muted text-muted-foreground border-border";
    }
  };

  const getStatusBadgeClasses = (status) => {
    switch (status) {
      case "PENDING":
        return "bg-yellow-100 text-yellow-600 border-yellow-300 dark:bg-yellow-900 dark:text-yellow-200";

      case "REJECTED":
        return "bg-red-100 text-red-600 border-red-300 dark:bg-red-900 dark:text-red-200";

      case "APPROVED":
        return "bg-emerald-100 text-emerald-600 border-emerald-300 dark:bg-emerald-900 dark:text-emerald-200";

      default:
        return "";
    }
  };

  return (
    <div className="relative">
      <div className="-mx-(--card-spacing) h-60 max-h-60 space-y-2.5 overflow-y-auto border-t px-(--card-spacing) py-4">
        {requests?.data?.length ? (
          requests.data.map((request) => (
            <div key={request.requestId} className={`rounded-xl border-2 px-5 py-2.5 transition-all duration-200 hover:opacity-90 ${getStatusClasses(request.status)}`}>
              <div className="flex w-full flex-col space-y-1">
                <span className="font-medium">User to admin promotion request</span>

                <div className="flex flex-col">
                  <span className="opacity-75">Requested on {new Date(request.createdAt).toLocaleString()}</span>
                  {request.status !== "PENDING" && <span className="opacity-75">Reviewed on {new Date(request.updatedAt).toLocaleString()}</span>}
                </div>

                <Badge className={`self-end mt-1 rounded-full border px-3 py-2.5 font-medium leading-relaxed tracking-wide ${getStatusBadgeClasses(request.status)}`}>{request.status}</Badge>
              </div>
            </div>
          ))
        ) : (
          <div className="flex h-32 items-center justify-center text-sm text-muted-foreground">No requests found.</div>
        )}
      </div>
      {requests?.data?.[0]?.status === "REJECTED" && (
        <Button onClick={handleRequestAdminAccess} disabled={isRequesting} className="absolute bottom-4 right-4 z-10">
          Request again
          <ArrowUpRight />
          {isRequesting ? <Spinner data-icon="inline-start" /> : ""}
        </Button>
      )}
    </div>
  );
};

export default RequestStatus;
