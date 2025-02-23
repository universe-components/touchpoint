import torch
import numpy as np
from PIL import Image
from transformers import AutoImageProcessor, AutoModel

# 提取 DINOv2 特征
def extract_dinov2_features(image_data, model):
    """
    从传感器传递的视觉影像中提取 DINOv2 特征。

    参数:
    image_data (numpy.ndarray): 传感器传递的视觉影像数据，形状为 (H, W, C)，其中 H 是高度，W 是宽度，C 是通道数。

    返回:
    features (numpy.ndarray): 提取的特征向量。
    """
    processor = AutoImageProcessor.from_pretrained(model)
    model = AutoModel.from_pretrained("facebook/dinov2-base")
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)

    # 将 NumPy 数组转换为 PIL 图像
    image = Image.fromarray(image_data.astype(np.uint8))

    # 将图像数据转换为模型需要的格式
    inputs = processor(images=image, return_tensors="pt").to(device)
    with torch.no_grad():
        outputs = model(**inputs)
        features = outputs.last_hidden_state.mean(dim=1).cpu().numpy()

    return features