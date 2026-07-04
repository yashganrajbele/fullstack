import React from "react";

const AuthInformation = () => {
  return (
    <div className="-mx-(--card-spacing) h-60 max-h-60 space-y-2.5 overflow-y-scroll border-t px-(--card-spacing) py-4 text-sm leading-relaxed font-light">
      <p>
        Authentication and Authorization Management System featuring role-based access control. Users can register, log in securely, and submit requests for administrator privileges. A Super Admin can review these promotion requests and either
        approve or reject them through a dedicated management interface.
      </p>
      <p>
        The system supports a complete promotion workflow, allowing users to re-submit requests after rejection and automatically granting admin privileges upon approval. Additionally, Super Admins can revoke administrative access by demoting admins
        back to regular users. The application includes secure authentication, role-based authorization, request tracking, status management, and a responsive user interface for efficient user and role management.
      </p>
    </div>
  );
};

export default AuthInformation;
