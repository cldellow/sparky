mkdir -p cldellow
s3cmd get --force s3://sortable-assets/misc/colin/iftop-1.0pre4 ./cldellow/iftop-1.0pre4
chmod a+x cldellow/iftop-1.0pre4
./cldellow/iftop-1.0pre4 -t -B -n -N -L 100
