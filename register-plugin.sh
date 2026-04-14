#!/bin/bash
# Register the Smart T&E backend API as a SalesHub plugin.
# Run once per tenant that wants to use the T&E module.
#
# Usage:
#   SALESHUB_TOKEN=<your_jwt> TENANT_ID=<your_tenant> ./register-plugin.sh
#
# After registration, note the returned `id` (UUID) — this is your proxy base path:
#   /plugins/{uuid}/api/...

SALESHUB_API="https://api.salescodeai.com"
TOKEN="${SALESHUB_TOKEN:?Set SALESHUB_TOKEN env var}"
TENANT="${TENANT_ID:?Set TENANT_ID env var}"

echo "Registering smart-te-api for tenant: $TENANT"

curl -s -X POST "$SALESHUB_API/plugins/register-json" \
  -H "Authorization: Bearer $TOKEN" \
  -H "X-Tenant-Id: $TENANT" \
  -H "Content-Type: application/json" \
  -d @plugin.json | tee /tmp/te-plugin-registration.json

echo ""
echo "Plugin UUID saved to /tmp/te-plugin-registration.json"
echo "Use the 'id' field as your proxy base: $SALESHUB_API/plugins/{id}/api/..."
