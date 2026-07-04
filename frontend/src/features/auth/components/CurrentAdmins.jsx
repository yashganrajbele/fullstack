import { Button } from "@/components/ui/button";
import { useDemoteAdminMutation, useGetAdminsQuery } from "../api/accountManagementApi";
import { Spinner } from "@/components/ui/spinner";
import { Skeleton } from "@/components/ui/skeleton";
import { useState } from "react";

const CurrentAdmins = () => {
  const { data: currentAdmins, isLoading: isGettingCurrentAdmins, error: getCurrentAdminsError } = useGetAdminsQuery();
  const [demoteAdmin, { isLoading: isDemoting }] = useDemoteAdminMutation();
  const [loadingAdminId, setLoadingAdminId] = useState(null);

  const handleDemoteAdmin = async (adminId) => {
    try {
      setLoadingAdminId(adminId);
      const response = await demoteAdmin(adminId).unwrap();
      if (response.success) {
        toast.success(response.message || "Admin demoted successfully.");
      }
    } catch (error) {
      toast.error("Failed to demote admin. Please try again.");
    } finally {
      setLoadingAdminId(null);
    }
  };

  return (
    <div className="-mx-(--card-spacing) h-60 max-h-60 space-y-2.5 overflow-y-auto border-t px-(--card-spacing) py-4">
      {isGettingCurrentAdmins ? (
        [1].map((key) => <Skeleton key={key} className="w-full h-[63.2px]" />)
      ) : currentAdmins?.data?.length ? (
        currentAdmins.data.map((admin) => (
          <div key={admin.id} className="bg-blue-50 text-blue-400 border-blue-100 dark:bg-blue-950 dark:text-blue-300 rounded-xl border-2 px-5 py-2.5 transition-all duration-200 hover:opacity-90 flex items-center justify-between">
            <div className="flex flex-col">
              <span className="font-medium">{admin.username}</span>
              <span className="text-sm text-muted-foreground">Administrator</span>
            </div>

            <Button onClick={() => handleDemoteAdmin(admin.id)} disabled={isDemoting} variant="destructive" size="sm">
              Demote
              {loadingAdminId === admin.id ? <Spinner data-icon="inline-start" /> : ""}
            </Button>
          </div>
        ))
      ) : (
        <div className="flex h-32 items-center justify-center text-sm text-muted-foreground">No administrators found.</div>
      )}
    </div>
  );
};

export default CurrentAdmins;
