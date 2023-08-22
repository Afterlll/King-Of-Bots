<template>
    <div class="container content-field">
        <div class="card">
            <div class="card-body">
                <!-- 未来要填充的都在slot标签里面 -->
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>
export default {

}
</script>

<style>
div.content-field {
    margin-top: 20px;
}

</style>