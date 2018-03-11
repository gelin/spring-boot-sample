.PHONY: run-nginx
run-nginx:
	docker run --rm --net=host -p 8000:8000 -v $(PWD)/etc/nginx.conf:/etc/nginx/conf.d/default.conf:ro nginx:stable-alpine
